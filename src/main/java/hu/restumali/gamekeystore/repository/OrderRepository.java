package hu.restumali.gamekeystore.repository;

import hu.restumali.gamekeystore.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
