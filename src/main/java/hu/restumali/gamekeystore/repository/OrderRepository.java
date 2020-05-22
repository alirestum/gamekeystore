package hu.restumali.gamekeystore.repository;

import hu.restumali.gamekeystore.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAll();

    OrderEntity findOneById(Long id);
}
