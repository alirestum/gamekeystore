package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.OrderEntity;
import hu.restumali.gamekeystore.model.OrderItemEntity;
import hu.restumali.gamekeystore.model.ProductEntity;
import hu.restumali.gamekeystore.repository.OrderItemRepository;
import hu.restumali.gamekeystore.repository.OrderRepository;
import hu.restumali.gamekeystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public List<OrderItemEntity> findAllByOrder(OrderEntity order) { return orderItemRepository.findAllByOrderOrderByProductAsc(order); }

    public void updateQuantity(Long orderId, Long productId, Integer quantity){
        OrderEntity managedOrder = orderRepository.findOneById(orderId);
        List<OrderItemEntity> managedItems = orderItemRepository.findAllByOrderOrderByProductAsc(managedOrder);
        for (OrderItemEntity item : managedItems){
            if (item.getProduct().getId().equals(productId))
                item.setQuantity(quantity);
        }
        managedOrder.setOrderSum(managedItems.stream().mapToInt(OrderItemEntity::getProductSum).sum());
        orderRepository.save(managedOrder);
        orderItemRepository.saveAll(managedItems);
    }

    public void removeItem(Long orderId, Long productId){
        List<OrderItemEntity> items = orderItemRepository.findAllByOrderOrderByProductAsc(orderRepository.findOneById(orderId));
        ProductEntity product = productRepository.findOneById(productId);
        items.remove(product);
        orderItemRepository.saveAll(items);
    }
}
