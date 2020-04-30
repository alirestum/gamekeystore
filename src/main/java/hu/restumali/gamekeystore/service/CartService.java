package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.OrderItem;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;


@Component
@SessionScope
public class CartService {

    @Getter
    private List<OrderItem> items;

    @Getter
    private Integer cartSum;

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
}
