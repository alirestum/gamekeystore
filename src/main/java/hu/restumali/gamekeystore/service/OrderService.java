package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.repository.OrderRepository;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    UserService userService;

    @Autowired
    OrderRepository orderRepository;

    public OrderItem createOrderItem(ProductEntity product){
        return new OrderItem(product);
    }

    public void createOrder(String userName, List<OrderItem> items, Integer orderSum, CouponEntity coupon){
        UserEntity user = userService.getUserByEmail(userName);
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatusType.Confirmed);
        order.setCustomer(user);
        order.setCoupon(coupon);
        order.setItems(items);
        order.setOrderSum(orderSum);
        userService.addOrderToUser(userName, order);
        orderRepository.save(order);
    }

    public List<OrderEntity> getOrdersByUser(String userName){
        return userService.getUserByEmail(userName).getOrders();
    }
}
