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
    public List<OrderItemEntity> findAllByOrder(OrderEntity order) { return orderItemRepository.findAllByOrder(order); }

    public void updateQuantity(Long orderId, Long productId, Integer quantity){
        List<OrderItemEntity> items = orderItemRepository.findAllByOrder(orderRepository.findOneById(orderId));
        for (OrderItemEntity item : items){
            if (item.getProduct().getId().equals(productId))
                item.setQuantity(quantity);
        }
        orderItemRepository.saveAll(items);
    }

    public void removeItem(Long orderId, Long productId){
        List<OrderItemEntity> items = orderItemRepository.findAllByOrder(orderRepository.findOneById(orderId));
        ProductEntity product = productRepository.findOneById(productId);
        items.remove(product);
        orderItemRepository.saveAll(items);
    }
}
