package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.service.OrderService;
import hu.restumali.gamekeystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@RequestMapping(value = "/user")
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/profile")
    public String profile(Map<String, Object> map){
        UserEntity user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getAddress() == null)
            map.put("address", new Address());
        else
            map.put("address", user.getAddress());
        return "profile";
    }


    @PreAuthorize("hasRole('ROLE_Customer') or hasRole('ROLE_WebshopAdmin')")
    @PostMapping(value = "/profile")
    public String profile(Map<String, Object> map,
                          @Valid Address address,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){

        if (!bindingResult.hasErrors()){
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.updateUserAddress(userName, address);
            return "redirect:/user/profile";
        } else {
            System.out.println(bindingResult);
            redirectAttributes.addFlashAttribute("address", address);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "address", bindingResult);
            return "profile";
        }



    }


    @GetMapping(value = "/register")
    public String register(Map<String, Object> map){
        map.putIfAbsent("user", new UserDTO());
        return "user-Register";
    }

    @PostMapping(value = "/register")
    public String register(@Valid UserDTO newUser,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if (!bindingResult.hasErrors() && newUser.getPassword().equals(newUser.getPasswordConfirm())){
            userService.registerNewUser(newUser);
            return "redirect:/";
        } else{
            if (!newUser.getPassword().equals(newUser.getPasswordConfirm()))
                bindingResult.addError(new FieldError("userDTO", "passwordConfirm", "Passwords do not match!"));
            redirectAttributes.addFlashAttribute("user", newUser);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
            return "redirect:/user/register";
        }
    }

    @GetMapping(value = "/changeinfo")
    public String changeUserInformation(Map<String, Object> map){
        UserDTO user = new UserDTO(userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        map.put("user", user);
        return "user-modifyAccount";
    }

    @PreAuthorize("hasAnyRole('Customer', 'WebshopAdmin')")
    @PostMapping(value = "/changeinfo")
    public String changeUserInformation(@Valid UserDTO userDTO,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        HttpSession session){
        if (!bindingResult.hasErrors() && userDTO.getPassword().equals(userDTO.getPasswordConfirm())){
            userService.modifyUser(userDTO, session);
            return "redirect:/user/profile";
        } else {
            if (!userDTO.getPassword().equals(userDTO.getPasswordConfirm()))
                bindingResult.addError(new FieldError("userDTO", "passwordConfirm", "Passwords do not match!"));
            redirectAttributes.addFlashAttribute("user", userDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
            return "redirect:/user/changeinfo";
        }
    }

    @GetMapping(value = "/login")
    public String login(){
        return "user-Login";
    }


    @PreAuthorize("hasAnyRole('Customer', 'WebshopAdmin')")
    @GetMapping(value = "/orders")
    public String orders(Map<String, Object> map){
        map.put("orders", orderService.getOrdersByUser(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "user-Orders";
    }

    @PreAuthorize("hasAnyRole('Customer', 'WebshopAdmin')")
    @GetMapping(value = "/rateproducts")
    public String rateProducts(Map<String, Object> map){
        List<OrderEntity> completedOrdersByUser = orderService.findCompletedOrdersByUser(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (OrderEntity o : completedOrdersByUser){
            orderItems.addAll(o.getItems());
        }
        HashSet<ProductEntity> products = new HashSet<>();
        for (OrderItemEntity oi : orderItems){
            if (!user.getRatedProducts().contains(oi.getProduct().getId()))
                products.add(oi.getProduct());
        }
        map.put("products", products);
        return "user-RateProducts";
    }

    @PreAuthorize("hasAnyRole('Customer', 'WebshopAdmin')")
    @PostMapping(value = "/rateproducts/{productId}")
    public String rateProducts(Map<String, Object> map,
                                @PathVariable("productId") Long productId,
                               @RequestParam("rating") Integer rating){

        this.userService.rateProduct(productId, SecurityContextHolder.getContext().getAuthentication().getName(), rating);
        List<OrderEntity> completedOrdersByUser = orderService.findCompletedOrdersByUser(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (OrderEntity o : completedOrdersByUser){
            orderItems.addAll(o.getItems());
        }
        HashSet<ProductEntity> products = new HashSet<>();
        for (OrderItemEntity oi : orderItems){
            if (!user.getRatedProducts().contains(oi.getProduct().getId()))
                products.add(oi.getProduct());
        }
        map.put("products", products);
        return "user-RateProducts";
    }

    @GetMapping(value = "/closeaccount")
    public String closeUserAccount(HttpSession session){
        userService.closeAccount(SecurityContextHolder.getContext().getAuthentication().getName());
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        session.removeAttribute("firstName");
        session.removeAttribute("lastName");
        return "redirect:/";
    }

}
