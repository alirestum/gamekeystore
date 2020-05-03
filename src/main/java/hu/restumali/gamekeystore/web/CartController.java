package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.OrderItem;
import hu.restumali.gamekeystore.model.ProductEntity;
import hu.restumali.gamekeystore.service.CartService;
import hu.restumali.gamekeystore.service.OrderService;
import hu.restumali.gamekeystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @GetMapping("")
    public String getCart(Map<String, Object> map){
        map.put("cartItems", cartService.getItems());
        map.put("cartSum", cartService.getCartSum());
        return "cart";
    }

    @PostMapping(value = "/addtocart")
    public ResponseEntity<String> addToCart(@RequestParam Long productId){
        OrderItem orderItem = orderService.createOrderItem(productService.findById(productId));
        cartService.addToCart(orderItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/applycoupon")
    public ResponseEntity<Integer> applyCoupon(@RequestParam String couponName){
        Integer newSum = cartService.applyCoupon(couponName);
        return new ResponseEntity<Integer>(newSum, HttpStatus.OK);
    }
}
