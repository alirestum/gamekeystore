package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.config.CouponException;
import hu.restumali.gamekeystore.model.OrderItemEntity;
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
        OrderItemEntity orderItemEntity = orderService.createOrderItem(productService.findById(productId));
        cartService.addToCart(orderItemEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/applycoupon")
    public ResponseEntity<Object> applyCoupon(@RequestParam String couponName){
        if (cartService.getCoupon() == null){
            Integer newSum;
            try {
                newSum = cartService.applyCoupon(couponName);
            } catch (CouponException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(newSum, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Coupon already applied!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "updatecount")
    public ResponseEntity<Object> updateCount(@RequestParam("id") Long id,
                                              @RequestParam("quantity") Integer quantity){
        cartService.updateItemCount(id, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "removeitem")
    public String removeItemFromCart(@RequestParam("id") Long id, Map<String, Object> map){
        cartService.removeFromCart(id);
        map.put("cartItems", cartService.getItems());
        map.put("cartSum", cartService.getCartSum());
        return "cart";
    }
}
