package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.config.CouponException;
import hu.restumali.gamekeystore.model.CouponEntity;
import hu.restumali.gamekeystore.model.OrderItem;
import hu.restumali.gamekeystore.repository.CouponRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;
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
        boolean found = false;
        for (OrderItem o : items) {
            if (o.getProduct().getId().equals(orderItem.getProduct().getId())){
                updateItemCount(orderItem.getProduct().getId());
                found = true;
                break;
            }
        }
        if (!found){
            items.add(orderItem);
            cartSum += orderItem.getProductSum();
        }
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

    public Integer applyCoupon(String name) throws CouponException {
        CouponEntity coupon = couponRepository.findOneByName(name);
        if (coupon == null)
            throw new CouponException("Coupon not found with name " + name);
        else if (coupon.getValidUntil() < System.currentTimeMillis())
            throw new CouponException("Coupon is no longer valid!");
        this.coupon = coupon;
        this.cartSum -= this.coupon.getDiscount();
        return this.cartSum;
    }

    public void updateItemCount(Long id) {
        items.forEach(orderItem -> {
            if (orderItem.getProduct().getId().equals(id)){
                orderItem.increaseQuantity();
                this.cartSum += orderItem.getProduct().getSalePrice() == null ? orderItem.getProduct().getBasePrice() : orderItem.getProduct().getSalePrice();
            }
        });
    }

    public void clearCart(){
        this.items.clear();
        this.coupon = null;
        this.cartSum = 0;
    }
}
