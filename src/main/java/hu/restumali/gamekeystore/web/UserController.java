package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.Address;
import hu.restumali.gamekeystore.model.UserDTO;
import hu.restumali.gamekeystore.model.UserEntity;
import hu.restumali.gamekeystore.model.UserRoleType;
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

import javax.validation.Valid;
import java.util.Map;

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

}
