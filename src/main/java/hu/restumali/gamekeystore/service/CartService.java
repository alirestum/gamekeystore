package hu.restumali.gamekeystore.service;

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

    public CartService(){
        this.items = new ArrayList<>();
        this.cartSum = 0;
    }

    public void addToCart(OrderItem orderItem){
        this.items.add(orderItem);
        this.cartSum += orderItem.getProductSum();
    }

    public void removeFromCart(OrderItem orderItem){
        this.items.remove(orderItem);
        this.cartSum -= orderItem.getProductSum();
    }

    public Integer applyCoupon(String name){
        this.coupon = couponRepository.findOneByName(name);
        this.cartSum -= this.coupon.getDiscount();
        return this.cartSum;
    }
}
