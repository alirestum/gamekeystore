package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.service.CartService;
import hu.restumali.gamekeystore.service.OrderService;
import hu.restumali.gamekeystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "/checkout")
public class CheckoutController {

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @GetMapping(value = "")
    public String checkOut(Map<String, Object> map){
        map.put("cartItems", cartService.getItems());
        map.put("orderTotal", cartService.getCartSum());
        map.put("coupon", cartService.getCoupon());
        return "checkout";
    }

    @PostMapping(value = "")
    public String placeOrder(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.createOrder(userName, cartService.getItems(), cartService.getCartSum(), cartService.getCoupon());
        return "redirect:/";
    }
}
