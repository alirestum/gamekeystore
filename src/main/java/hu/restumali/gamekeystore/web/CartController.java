package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.service.CartService;
import hu.restumali.gamekeystore.service.OrderService;
import hu.restumali.gamekeystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String getCart(Map<String, Object> map){
        return "cart";
    }

    @PostMapping("/addtocart")
    public String addToCart(@RequestParam Integer productId){

    }
}
