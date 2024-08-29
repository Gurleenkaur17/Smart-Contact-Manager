package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.service.UserService;

@ControllerAdvice
public class RootController {

        @Autowired
        private UserService userService;

        @ModelAttribute
        public void addLoggedInUserInfo(Model model, Authentication authentication){
            if(authentication == null){
                model.addAttribute("loggedInUser", null);
                return;
            } 
            
            String email = Helper.getEmailOfLoggedInUser(authentication);
            User user = userService.getUserByEmail(email);
            model.addAttribute("loggedInUser", user);
            System.out.println(user);
        }
}
