package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.service.CouponService;
import hu.restumali.gamekeystore.service.FileService;
import hu.restumali.gamekeystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.lang.model.element.ModuleElement;
import javax.validation.Valid;
import java.text.DateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    FileService fileService;

    @Autowired
    ProductService productService;

    @Autowired
    CouponService couponService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminHomePage(){
        return "/admin";
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String products(Map<String, Object> map){
        map.put("productsList", productService.findAll());
        return "admin-products";
    }

    @RequestMapping(value = "/addproduct", method = RequestMethod.GET)
    public String addProduct(Map<String, Object> map){

        map.putIfAbsent("newProduct", new ProductEntity());
        map.put("platformTypes", EnumSet.allOf(PlatformType.class));
        map.put("styles", EnumSet.allOf(GameStyleType.class));
        map.put("availabilityOptions", EnumSet.allOf(ProductAvailabilityType.class));
        return "admin-newProduct";
    }

    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute("newProduct")
                             @Valid ProductEntity newProduct,
                             @RequestParam("featuredImage") MultipartFile featuredImage,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){


        if(!bindingResult.hasErrors()){
            String filename = fileService.uploadFile(featuredImage);
            newProduct.setFeaturedImageUrl(filename);
            productService.save(newProduct);

            return "redirect:products";
        }
        else {
            redirectAttributes.addFlashAttribute("newProduct", newProduct);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newProduct", bindingResult);
            return "redirect:addproduct";
        }

    }

    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public String coupons(Map<String, Object> map){
        List<CouponEntityDTO> coupons = new ArrayList<>();
        List<CouponEntity> c = couponService.findAll();
        for (CouponEntity cc : c) {
            coupons.add(new CouponEntityDTO(cc, cc.getOrders().size()));
        }
        map.put("couponsList", coupons);
        return "admin-coupons";
    }

    @RequestMapping(value = "/addcoupon", method = RequestMethod.GET)
    public String addCoupon(Map<String, Object> map){
        map.putIfAbsent("newCoupon", new CouponEntity());
        return "admin-newCoupon";
    }

    @RequestMapping(value = "/addcoupon", method = RequestMethod.POST)
    public String addCoupon(@ModelAttribute("newCoupon")
                            CouponEntityDTO newCoupon,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){


        if(!bindingResult.hasErrors()){
            couponService.createCouponEntity(newCoupon);
            return "redirect:coupons";
        } else{
            System.out.println(bindingResult);
            System.out.println("vanerror");
            redirectAttributes.addFlashAttribute("newCoupon", newCoupon);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newCoupon", bindingResult);
            return "redirect:addcoupon";
        }

    }
}
