package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.config.CouponNotFoundException;
import hu.restumali.gamekeystore.model.CouponEntity;
import hu.restumali.gamekeystore.model.OrderItem;
import hu.restumali.gamekeystore.repository.CouponRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;


@Component
@SessionScope
public class CartService {

    @Autowired
    CouponRepository couponRepository;

    @Getter
    private List<OrderItem> items;

    @Getter
    private Integer cartSum;

    @Getter
    private CouponEntity coupon;

    public CartService() {
        this.items = new ArrayList<>();
        this.cartSum = 0;
    }

    public void addToCart(OrderItem orderItem) {
        this.items.add(orderItem);
        this.cartSum += orderItem.getProductSum();
    }

    public void removeFromCart(Long id) {
        OrderItem removeItem = null;
        for (OrderItem o : items){
            if (o.getProduct().getId().equals(id)){
                removeItem = o;
                break;
            }
        }

        this.items.remove(removeItem);
        this.cartSum -= removeItem.getProductSum();
    }

    public Integer applyCoupon(String name) throws CouponNotFoundException {
        this.coupon = couponRepository.findOneByName(name);
        if (this.coupon == null)
            throw new CouponNotFoundException("Coupon not found with name " + name);
        this.cartSum -= this.coupon.getDiscount();
        return this.cartSum;
    }

    public void updateItemCount(Long id) {
        items.forEach(orderItem -> {
            if (orderItem.getProduct().getId().equals(id))
                orderItem.increaseQuantity();
        });
    }
}
