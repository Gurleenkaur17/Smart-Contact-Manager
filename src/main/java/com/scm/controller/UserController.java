package com.scm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {

    // @Autowired
    // private UserService userService;
    
    // @PostMapping("/dashboard")
    // public String postUserDashboard(){
    //     return "user/dashboard";
    // }

    // @PostMapping("/profile")
    // public String PostUserPrfile(){
    //     return "user/profile";
    // }
    @RequestMapping("/dashboard")
    public String GetUserDashboard(){
        return "user/dashboard";
    }

    @RequestMapping("/profile")
    public String GetUserPrfile(Model model,Authentication authentication){
        
        return "user/profile";
    }
}
