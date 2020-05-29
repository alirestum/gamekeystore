package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.config.NoValidBillingAddressException;
import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.repository.OrderItemRepository;
import hu.restumali.gamekeystore.repository.OrderRepository;
import hu.restumali.gamekeystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    UserService userService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    public OrderItemEntity createOrderItem(ProductEntity product, OrderEntity order){
        OrderItemEntity orderItemEntity = new OrderItemEntity(product);
        orderItemEntity.setOrder(order);
        orderItemRepository.save(orderItemEntity);
        return orderItemEntity;
    }

    public void createOrder(String userName, List<OrderItemEntity> items, Integer orderSum, CouponEntity coupon){
        UserEntity user = userService.getUserByEmail(userName);
        if (user.getAddress() == null)
            throw new NoValidBillingAddressException("Please set a valid billing address");
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatusType.Confirmed);
        order.setCustomer(user);
        order.setCoupon(coupon);
        order.setItems(items);
        order.setOrderSum(orderSum);
        order.setOrderDate(LocalDateTime.now());
        order.setBillingAddress(user.getAddress());
        userService.addOrderToUser(userName, order);
        orderRepository.save(order);
        for (OrderItemEntity item : items){
            item.setOrder(order);
            orderItemRepository.save(item);
        }
    }

    public List<OrderEntity> getOrdersByUser(String userName){
        return userService.getUserByEmail(userName).getOrders();
    }

    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    public void deleteOrderById(Long id){
        OrderEntity managedEntity = orderRepository.findOneById(id);
        managedEntity.setStatus(OrderStatusType.Deleted);
        orderRepository.save(managedEntity);
    }

    public OrderEntity findById(Long id){
        return orderRepository.findOneById(id);
    }

    public void updateOrder(Long id, OrderEntity updatedOrder){
        OrderEntity managedOrder = orderRepository.findOneById(id);
        managedOrder.setStatus(updatedOrder.getStatus());
        managedOrder.setBillingAddress(updatedOrder.getBillingAddress());
        orderRepository.save(managedOrder);
    }

    public void removeOrderItem(Long orderId, Long productId){
        OrderEntity managedOrder = orderRepository.findOneById(orderId);
        OrderItemEntity orderItemEntity = managedOrder.getItems().stream().filter(it -> it.getProduct().getId().equals(productId)).findFirst().get();
        managedOrder.getItems().removeIf(it -> it.getProduct().getId().equals(productId));
        orderItemRepository.delete(orderItemEntity);
        managedOrder.setOrderSum(managedOrder.getItems().stream().mapToInt(OrderItemEntity::getProductSum).sum());
        orderRepository.save(managedOrder);
    }

    public void addOrderItem(Long orderId, Long productId){
        OrderEntity managedOrder = orderRepository.findOneById(orderId);
        ProductEntity newProduct = productRepository.findOneById(productId);
        OrderItemEntity newItem = this.createOrderItem(newProduct, managedOrder);
        managedOrder.getItems().add(newItem);
        orderRepository.save(managedOrder);
    }

    public List<OrderEntity> findCompletedOrdersByUser(String userName){
        UserEntity user = userService.getUserByEmail(userName);
        return orderRepository.findAllByCustomerAndStatus(user, OrderStatusType.Completed);
    }
}
