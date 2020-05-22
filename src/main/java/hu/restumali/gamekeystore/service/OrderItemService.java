package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.OrderEntity;
import hu.restumali.gamekeystore.model.OrderItemEntity;
import hu.restumali.gamekeystore.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Transactional
    public List<OrderItemEntity> findAllByOrder(OrderEntity order) { return orderItemRepository.findAllByOrder(order); }
}
