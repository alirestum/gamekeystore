package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.config.CouponException;
import hu.restumali.gamekeystore.model.CouponEntity;
import hu.restumali.gamekeystore.model.OrderItemEntity;
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
    private List<OrderItemEntity> items;

    @Getter
    private Integer cartSum;

    @Getter
    private CouponEntity coupon;

    public CartService() {
        this.items = new ArrayList<>();
        this.cartSum = 0;
    }

    public void addToCart(OrderItemEntity orderItemEntity) {
        boolean found = false;
        for (OrderItemEntity o : items) {
            if (o.getProduct().getId().equals(orderItemEntity.getProduct().getId())){
                updateItemCount(orderItemEntity.getProduct().getId(), 1);
                found = true;
                break;
            }
        }
        if (!found){
            items.add(orderItemEntity);
            cartSum += orderItemEntity.getProductSum();
        }
    }

    public void removeFromCart(Long id) {
        OrderItemEntity removeItem = null;
        for (OrderItemEntity o : items){
            if (o.getProduct().getId().equals(id)){
                removeItem = o;
                break;
            }
        }

        this.items.remove(removeItem);
        this.cartSum -= removeItem.getProductSum();
    }

    public Integer applyCoupon(String name) throws CouponException {
        CouponEntity coupon = couponRepository.findOneByNameAndDeleted(name, false);
        if (coupon == null)
            throw new CouponException("Coupon not found with name " + name);
        else if (coupon.getValidUntil() < System.currentTimeMillis())
            throw new CouponException("Coupon is no longer valid!");
        this.coupon = coupon;
        this.cartSum -= this.coupon.getDiscount();
        return this.cartSum;
    }

    public OrderItemEntity updateItemCount(Long id, Integer quantity) {
        items.forEach(orderItem -> {
            if (orderItem.getProduct().getId().equals(id)){
                orderItem.updateQuantity(quantity);
                this.cartSum += orderItem.getProductSum();
            }
        });
       return items.stream().filter(it -> it.getProduct().getId().equals(id)).findFirst().get();
    }

    public void clearCart(){
        this.items.clear();
        this.coupon = null;
        this.cartSum = 0;
    }
}
