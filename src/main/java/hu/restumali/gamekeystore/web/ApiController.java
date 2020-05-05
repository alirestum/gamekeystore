package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.GameCategories;
import hu.restumali.gamekeystore.model.PlatformType;
import hu.restumali.gamekeystore.model.ProductAvailabilityType;
import hu.restumali.gamekeystore.model.ProductEntity;
import hu.restumali.gamekeystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getProducts(Map<String, Object> map, Pageable pageable) {
        List<PlatformType> platforms = Arrays.asList(PlatformType.values());
        List<GameCategories> gameCategories = Arrays.asList(GameCategories.values());
        Integer maxPrice = Integer.MAX_VALUE;

        createMapForFilter(maxPrice, pageable, map, platforms, gameCategories);
        return "products";
    }

    @GetMapping(value = "/products/filter")
    public String filterProducts(@RequestParam("platform") String platform,
                                 @RequestParam("maxprice") Integer maxPrice,
                                 @RequestParam("categories") List<String> categories,
                                 Pageable pageable,
                                 Map<String, Object> map) {
        List<PlatformType> platforms;
        if (platform.equals("All") | platform.equals(""))
            platforms = Arrays.asList(PlatformType.values());
        else {
            platforms = new ArrayList<>();
            platforms.add(PlatformType.valueOf(platform));
        }
        if (maxPrice == null)
            maxPrice = Integer.MAX_VALUE;

        List<GameCategories> gameCategories;
        if (!categories.isEmpty()) {
            gameCategories = categories.stream()
                    .map(GameCategories::valueOf).collect(Collectors.toList());
        }
        else
            gameCategories = Arrays.asList(GameCategories.values());

        createMapForFilter(maxPrice, pageable, map, platforms, gameCategories);
        return "/fragments/productsFragment :: productsList";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable("id") Long id,
                                 Map<String, Object> map){
        map.put("product", productService.findById(id));
        return "productDetails";
    }

    private void createMapForFilter(@RequestParam("maxprice") Integer maxPrice, Pageable pageable, Map<String, Object> map, List<PlatformType> platforms, List<GameCategories> gameCategories) {
        Page<ProductEntity> products = productService.filter(platforms, gameCategories, maxPrice, pageable);
        List<Integer> pageNum = new ArrayList<>();
        for (int i = 1; i <= products.getTotalPages(); ++i) {
            pageNum.add(i);
        }
        map.put("products", products.getContent());
        map.put("pages", pageNum);
        map.put("currentPage", products.getNumber() + 1);
        map.put("categories", EnumSet.allOf(GameCategories.class));
        map.put("platformTypes", EnumSet.allOf(PlatformType.class));
        map.put("maxPrice", productService.highestPrice());
    }


}
