package hu.restumali.gamekeystore.web;

import hu.restumali.gamekeystore.model.UserEntity;
import hu.restumali.gamekeystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    UserService userService;


    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "redirect:/api/products";
    }

    @GetMapping("")
    public void giveAdmin(@RequestParam Boolean givemeAdmin){
        if (givemeAdmin == true){
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.giveAdmin(username);
        }
    }
}
