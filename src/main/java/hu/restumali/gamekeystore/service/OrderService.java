package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.OrderItem;
import hu.restumali.gamekeystore.model.ProductEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    public OrderItem createOrderItem(ProductEntity product){
        return new OrderItem(product);
    }
}
