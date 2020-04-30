package hu.restumali.gamekeystore.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.service.CouponService;
import hu.restumali.gamekeystore.service.FileService;
import hu.restumali.gamekeystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ObjectMapper objectMapper;

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

    @GetMapping(value = "products/addproduct")
    public String addProduct(Map<String, Object> map){

        map.putIfAbsent("newProduct", new ProductEntity());
        map.put("platformTypes", EnumSet.allOf(PlatformType.class));
        map.put("styles", EnumSet.allOf(GameCategories.class));
        map.put("availabilityOptions", EnumSet.allOf(ProductAvailabilityType.class));
        map.put("url", "/admin/products/addproduct");
        return "admin-productForm";
    }

    @PostMapping(value = "products/addproduct")
    public String addProduct(@ModelAttribute("newProduct")
                             @Valid ProductEntity newProduct,
                             @RequestParam("featuredImage") MultipartFile featuredImage,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){


        if(!bindingResult.hasErrors()){
            String filename = fileService.uploadFile(featuredImage);
            newProduct.setFeaturedImageUrl(filename);
            productService.save(newProduct);

            return "redirect:/admin/products";
        }
        else {
            redirectAttributes.addFlashAttribute("newProduct", newProduct);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newProduct", bindingResult);
            return "redirect:addproduct";
        }

    }

    @GetMapping(value = "/products/{id}/update")
    public String updateProduct(@PathVariable("id") Long productId,
                                Map<String, Object> map){
        map.put("newProduct", productService.findById(productId));
        map.put("platformTypes", EnumSet.allOf(PlatformType.class));
        map.put("styles", EnumSet.allOf(GameCategories.class));
        map.put("availabilityOptions", EnumSet.allOf(ProductAvailabilityType.class));
        map.put("url", "/admin/products/" + productId + "/update");
        return "admin-productForm";
    }

    @PostMapping(value = "/products/{id}/update")
    public String updateProduct(@PathVariable("id") Long productId,
                                @Valid ProductEntity newProduct,
                                @RequestParam("featuredImage") MultipartFile featuredImage,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes){
        System.out.println(featuredImage);
        if(!bindingResult.hasErrors()){
            if (!featuredImage.isEmpty()){
                String filename = fileService.uploadFile(featuredImage);
                newProduct.setFeaturedImageUrl(filename);
            }
            productService.updateProductById(productId, newProduct);
            return "redirect:/admin/products";
        }
        else {
            redirectAttributes.addFlashAttribute("newProduct", newProduct);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newProduct", bindingResult);
            return "redirect:/admin/" + newProduct.getId() + "/update";
        }
    }

    @GetMapping(value = "/products/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long productId){
        productService.deleteById(productId);
        return "redirect:/admin/products";
    }

    @GetMapping("/importproducts")
    public String importProducts(){
        return "admin-importProducts";
    }

    @PostMapping("/importproducts")
    public String importProducts(@RequestParam("importFile") MultipartFile file){
        List<ProductEntity> importedProducts = fileService.importFile(file);
        importedProducts.forEach(productEntity -> productService.save(productEntity));
        return "redirect:products";
    }

    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public String coupons(Map<String, Object> map){
        List<CouponEntity> coupons = couponService.findAll();
        map.put("couponsList", coupons);
        return "admin-coupons";
    }

    @RequestMapping(value = "/coupons/addcoupon", method = RequestMethod.GET)
    public String addCoupon(Map<String, Object> map){
        map.putIfAbsent("newCoupon", new CouponEntity());
        map.put("url", "/admin/coupons/addcoupon");
        return "admin-couponForm";
    }

    @RequestMapping(value = "/coupons/addcoupon", method = RequestMethod.POST)
    public String addCoupon(@ModelAttribute("newCoupon")
                            CouponEntityDTO newCoupon,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){


        if(!bindingResult.hasErrors()){
            couponService.createCouponEntity(newCoupon);
            return "redirect:/admin/coupons";
        } else{
            redirectAttributes.addFlashAttribute("newCoupon", newCoupon);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newCoupon", bindingResult);
            return "redirect:/admin/coupons/addcoupon";
        }

    }

    @GetMapping(value = "/coupons/{id}/update")
    public String updateCoupon(@PathVariable("id") Long couponId,
                               Map<String, Object> map){
        CouponEntity coupon = couponService.findById(couponId);
        CouponEntityDTO dto = new CouponEntityDTO(coupon);
        map.putIfAbsent("newCoupon", coupon);
        map.put("url", "/admin/coupons/" + couponId + "/update");
        return "admin-couponForm";
    }

    @PostMapping(value = "coupons/{id}/update")
    public String updateCoupon(@PathVariable("id") Long couponId,
                               @Valid CouponEntityDTO newCoupon,
                               RedirectAttributes redirectAttributes,
                               BindingResult bindingResult){
        if(!bindingResult.hasErrors()){
            couponService.updateCouponById(couponId, newCoupon);
            return "redirect:/admin/coupons";
        } else{
            redirectAttributes.addFlashAttribute("newCoupon", newCoupon);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newCoupon", bindingResult);
            return "redirect:/admin/coupons/addcoupon";
        }
    }


    @GetMapping(value = "coupons/{id}/delete")
    public String deleteCoupon(@PathVariable("id") Long couponId){
        couponService.deleteOneById(couponId);
        return "redirect:/admin/coupons";
    }
}
