package hu.restumali.gamekeystore.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.service.ProductService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getProducts(Map<String, Object> map, Pageable pageable) {
        List<PlatformType> platforms = List.of(PlatformType.values());
        List<GameCategories> gameCategories = List.of(GameCategories.values());
        Integer maxPrice = Integer.MAX_VALUE;
        List<AgeLimitType> ageLimits = List.of(AgeLimitType.values());

        createMapForFilter(maxPrice, pageable, map, platforms, gameCategories, ageLimits);
        return "products";
    }

    @GetMapping(value = "/products/filter")
    public String filterProducts(@RequestParam("platform") String platform,
                                 @RequestParam("maxprice") Integer maxPrice,
                                 @RequestParam("categories") List<String> categories,
                                 @RequestParam("agelimit") String ageLimit,
                                 Pageable pageable,
                                 Map<String, Object> map) {
        List<PlatformType> platforms;
        if (platform.equals("All") || platform.equals(""))
            platforms = Arrays.asList(PlatformType.values());
        else {
            platforms = new ArrayList<>();
            platforms.add(PlatformType.valueOf(platform));
        }
        List<AgeLimitType> ageLimits;
        System.out.println(ageLimit);
        if (ageLimit.equals("All") || ageLimit.equals(""))
            ageLimits = Arrays.asList(AgeLimitType.values());
        else {
            ageLimits = new ArrayList<>();
            ageLimits.add(AgeLimitType.valueOf(ageLimit));
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

        createMapForFilter(maxPrice, pageable, map, platforms, gameCategories, ageLimits);
        return "/fragments/productsFragment :: productsList";
    }

    @GetMapping("/products/{id}")
    public ModelAndView productDetails(@PathVariable("id") Long id){
        ModelAndView mw = new ModelAndView("productDetails");
        ProductEntity product = productService.findById(id);
        Integer ratingCnt = product.getRatings().size();
        Double avgRating = product.getRatings().stream().mapToDouble(Integer::doubleValue).average().orElse(Double.NaN);
        mw.addObject("ratingCnt", ratingCnt);
        mw.addObject("avgRating", avgRating);
        mw.addObject("product", product);

        return mw;
    }

    @GetMapping(value = "products/search")
    public String searchProducts(){
        return "productsSearch";
    }

    @PostMapping(value = "/products/search")
    public String searchProducts(@RequestParam("name") String name,
                                                 Map<String, Object> map){
        List<ProductEntity> products = productService.searchByName("%" + name + "%");
        map.put("products", products);
        return "/fragments/searchFragment :: searchResult";
    }

    private void createMapForFilter(@RequestParam("maxprice") Integer maxPrice, Pageable pageable,
                                    Map<String, Object> map, List<PlatformType> platforms,
                                    List<GameCategories> gameCategories, List<AgeLimitType> ageLimits) {
        Page<ProductEntity> products = productService.filter(platforms, gameCategories, maxPrice, ageLimits, pageable);
        List<Integer> pageNum = new ArrayList<>();
        for (int i = 1; i <= products.getTotalPages(); ++i) {
            pageNum.add(i);
        }
        map.put("products", products.getContent());
        map.put("pages", pageNum);
        map.put("currentPage", products.getNumber() + 1);
        map.put("categories", EnumSet.allOf(GameCategories.class));
        map.put("platformTypes", EnumSet.allOf(PlatformType.class));
        map.put("ageLimitTypes", EnumSet.allOf(AgeLimitType.class));
        map.put("maxPrice", productService.highestPrice());
    }




}
