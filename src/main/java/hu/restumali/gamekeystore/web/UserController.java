package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.Address;
import hu.restumali.gamekeystore.model.UserDTO;
import hu.restumali.gamekeystore.model.UserEntity;
import hu.restumali.gamekeystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RequestMapping(value = "/user")
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/profile")
    public String profile(Map<String, Object> map){
        UserEntity user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getAddress() == null)
            map.put("address", new Address());
        else
            map.put("address", user.getAddress());
        return "profile";
    }

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
    public String register(Map<String, Object> map,
                           @Valid UserDTO newUser,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if (!bindingResult.hasErrors()){
            userService.registerNewUser(newUser);
            return "redirect:/";
        } else{
            redirectAttributes.addFlashAttribute("newProduct", newUser);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newUser", bindingResult);
            return "redirect:/user/register";
        }
    }

    @GetMapping(value = "/login")
    public String login(){
        return "user-Login";
    }

}
