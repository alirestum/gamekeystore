package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.CouponEntity;
import hu.restumali.gamekeystore.model.CouponEntityDTO;
import hu.restumali.gamekeystore.repository.CouponRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public void save(CouponEntity couponEntity) { couponRepository.save(couponEntity); }

    public List<CouponEntity> findAll() { return couponRepository.findAll(); }

    public void deleteOneById(Long id) { couponRepository.deleteById(id); }

    public CouponEntity findById(Long id){ return couponRepository.findOneById(id); }

    @SneakyThrows
    public void createCouponEntity(CouponEntityDTO newCoupon){
        CouponEntity coupon = new CouponEntity();
        coupon.setDiscount(newCoupon.getDiscount());
        coupon.setName(newCoupon.getName());
        coupon.setValidUntil(new SimpleDateFormat("yyyy-MM-dd").parse(newCoupon.getValidUntil()).getTime());
        couponRepository.save(coupon);
    }

    @SneakyThrows
    public void updateCouponById(Long id, CouponEntityDTO newCoupon){
        CouponEntity managedCoupon = couponRepository.findOneById(id);
        managedCoupon.setName(newCoupon.getName());
        managedCoupon.setDiscount(newCoupon.getDiscount());
        managedCoupon.setValidUntil(new SimpleDateFormat("yyyy-MM-dd").parse(newCoupon.getValidUntil()).getTime());
        couponRepository.save(managedCoupon);
    }
}
