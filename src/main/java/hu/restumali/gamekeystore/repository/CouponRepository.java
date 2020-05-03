package hu.restumali.gamekeystore.repository;

import hu.restumali.gamekeystore.model.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    List<CouponEntity> findAll();

    CouponEntity findOneById(Long id);

    CouponEntity findOneByName(String name);
}
