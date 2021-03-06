package hu.restumali.gamekeystore.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.service.*;
import lombok.Getter;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_WebshopAdmin')")
public class AdminController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileService fileService;

    @Autowired
    ProductService productService;

    @Autowired
    CouponService couponService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

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
        map.put("ageLimitOptions", EnumSet.allOf(AgeLimitType.class));
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
        map.put("ageLimitOptions", EnumSet.allOf(AgeLimitType.class));
        map.put("url", "/admin/products/" + productId + "/update");
        return "admin-productForm";
    }

    @PostMapping(value = "/products/{id}/update")
    public String updateProduct(@PathVariable("id") Long productId,
                                @Valid ProductEntity newProduct,
                                @RequestParam("featuredImage") MultipartFile featuredImage,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes){
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

    @GetMapping(value = "/products/search")
    public String searchProducts(@RequestParam("name") String name,
                                 Map<String, Object> map){
        List<ProductEntity> products = productService.searchByName("%" + name + "%");
        map.put("products", products);
        return "/fragments/admin-searchFragment :: admin-searchResult";
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

    @GetMapping("/exportproducts")
    public String exportProducts(){
        List<ProductEntity> products = productService.findAll();
        fileService.exportProducts(products);
        return "redirect:products";
    }

    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public String coupons(Map<String, Object> map){
        List<CouponEntity> coupons = couponService.findAllNotDeleted();
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
                            @Valid CouponEntityDTO newCoupon,
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

    @PostMapping(value = "/coupons/{id}/update")
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


    @GetMapping(value = "/coupons/{id}/delete")
    public String deleteCoupon(@PathVariable("id") Long couponId){
        couponService.deleteOneById(couponId);
        return "redirect:/admin/coupons";
    }

    @GetMapping(value = "/orders")
    public String getOrders(Map<String, Object> map){
        List<OrderEntity> orders = orderService.getAllOrders();
        map.put("orders", orders);
        return "admin-orders";
    }

    @GetMapping(value = "/orders/{id}/delete")
    public String deleteOrder(@PathVariable("id") Long orderId){
        orderService.deleteOrderById(orderId);
        return "redirect:/admin/orders";
    }

    @GetMapping(value = "/orders/addorder")
    public ModelAndView addOrder(){
        ModelAndView mw = new ModelAndView("admin-orderForm");
        mw.addObject("url", "/admin/orders/addorder");
        mw.addObject("order", new OrderEntity());
        mw.addObject("orderStatusType", EnumSet.allOf(OrderStatusType.class));
        return mw;
    }

    @PostMapping(value = "/orders/addorder")
    public String addOrder(Map<String, Object> map,
                           OrderEntity newOrder){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.createOrder(username, new ArrayList<>(), 0, null);
        return "redirect:/admin/orders";
    }
//TODO formok cimei szarok
    @GetMapping(value = "/orders/{id}/update")
    public String updateOrder(@PathVariable("id") Long orderId,
                              Map<String, Object> map){
        OrderEntity order = orderService.findById(orderId);
        map.put("order", order);
        map.put("orderStatusType", EnumSet.allOf(OrderStatusType.class));
        map.put("url", "/admin/orders/" + orderId + "/update");
        return "admin-orderForm";
    }

    @PostMapping(value = "/orders/{id}/update")
    public String updateOrder(@PathVariable("id") Long orderId,
                              OrderEntity order){
        orderService.updateOrder(orderId, order);
        return "redirect:/admin/orders";
    }

    @GetMapping(value = "/orders/{id}/products")
    public String getProductsByOrder(@PathVariable("id") Long id,
                                           Map<String, Object> map){
        OrderEntity order = orderService.findById(id);
        List<OrderItemEntity> orderItems = orderItemService.findAllByOrder(order);
        map.put("orderItems", orderItems);
        return "admin-order-products";
    }

    @PostMapping(value = "/orders/{orderId}/products/{productId}/updatequantity")
    public String updateOrderItemQuantity(@PathVariable("orderId") Long orderId,
                                          @PathVariable("productId") Long productId,
                                          @RequestParam("quantity") Integer quantity,
                                          Map<String, Object> map){
        orderItemService.updateQuantity(orderId, productId, quantity);
        List<OrderItemEntity> orderItems = orderItemService.findAllByOrder(orderService.findById(orderId));
        map.put("orderItems", orderItems);
        return "/fragments/admin-order-productsFragment.html :: order-productsList";
    }

    @PostMapping(value = "orders/{orderId}/products/{productId}/removeproduct")
    public String removeOrderItem(@PathVariable("orderId") Long orderId,
                                  @PathVariable("productId") Long productId,
                                  Map<String, Object> map){
        orderService.removeOrderItem(orderId, productId);
        List<OrderItemEntity> orderItems = orderItemService.findAllByOrder(orderService.findById(orderId));
        map.put("orderItems", orderItems);
        return "fragments/admin-order-productsFragment.html :: order-productsList";
    }

    @PostMapping(value = "orders/{orderId}/products/{productId}/add")
    public ResponseEntity<String> addOrderItem(@PathVariable("orderId") Long orderId,
                               @PathVariable("productId") Long productId){
        orderService.addOrderItem(orderId, productId);
        return new ResponseEntity<>("Product successfully added to the order!", HttpStatus.OK);
    }

}
