package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.OrderItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;


@Component
@SessionScope
public class CartService {

    private List<OrderItem> products;

    private Integer cartSum;

    public CartService(){
        this.products = new ArrayList<>();
        this.cartSum = 0;
    }

    public void addToCart(OrderItem orderItem){
        this.products.add(orderItem);
        this.cartSum += orderItem.getProductsum();
    }

    public void removeFromCart(OrderItem orderItem){
        this.products.remove(orderItem);
        this.cartSum -= orderItem.getProductsum();
    }
}
