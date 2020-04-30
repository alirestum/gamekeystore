package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.GameCategories;
import hu.restumali.gamekeystore.model.PlatformType;
import hu.restumali.gamekeystore.model.ProductEntity;
import hu.restumali.gamekeystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getProducts(Map<String, Object> map){
        List<ProductEntity> products = productService.findAll();
        map.put("products", products);
        map.put("categories", EnumSet.allOf(GameCategories.class));
        map.put("platformTypes", EnumSet.allOf(PlatformType.class));
        map.put("maxPrice", productService.highestPrice());
        return "products";
    }

    @GetMapping(value = "/products/filter")
    public String filterProducts(@RequestParam("platform") String platform,
                                 @RequestParam("maxprice") Integer maxPrice,
                                 @RequestParam("categories") List<String> categories,
                                 Map<String, Object> map){
        List<PlatformType> platforms= null;
        System.out.println("Platform: " + platform + "Maxprice: " +  maxPrice + "Categories: " + categories.isEmpty());
        if (platform.equals("All") | platform.equals(""))
            platforms = Arrays.asList(PlatformType.values());
        else {
            platforms = new ArrayList<>();
            platforms.add(PlatformType.valueOf(platform));
        }
        if (maxPrice == null)
            maxPrice = Integer.MAX_VALUE;

        List<GameCategories> gameCategories = null;
        if (!categories.isEmpty()) {
            gameCategories = categories.stream()
                    .map(GameCategories::valueOf).collect(Collectors.toList());
        }
        else
            gameCategories = Arrays.asList(GameCategories.values());

        List<ProductEntity> products = productService.filter(platforms, gameCategories, maxPrice);
        map.put("products", products);
        return "/fragments/productsFragment :: productsList";
    }
}
