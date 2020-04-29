package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.GameStyleType;
import hu.restumali.gamekeystore.model.PlatformType;
import hu.restumali.gamekeystore.model.ProductEntity;
import hu.restumali.gamekeystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getProducts(Map<String, Object> map){
        List<ProductEntity> products = productService.findAll();
        products.forEach(x -> x.setFeaturedImageUrl("/cnd/" + x.getFeaturedImageUrl()));
        map.put("products", products);
        map.put("categories", EnumSet.allOf(GameStyleType.class));
        map.put("platformTypes", EnumSet.allOf(PlatformType.class));
        return "products";
    }
}
