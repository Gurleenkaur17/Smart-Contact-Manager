package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.User;
import com.scm.entities.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class ClientController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @RequestMapping("about")
    public String aboutPage(){
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("services")
    public String servicePage(){
        System.out.println("Services page");
        return "services";
    }

    @RequestMapping("contact")
    public String contactPage(){
        System.out.println("contacts page");
        return "contact";
    }

    @RequestMapping("login")
    public String loginPage(){
        System.out.println("login page");
        return "login";
    }

    @RequestMapping("signup")
    public String signupPage(Model model){
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        System.out.println("userform" + userForm);

        return "signup";
    }

    @PostMapping("/do-register")
    public String register(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session){
        
        if(bindingResult.hasErrors()){
            return "signup";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setPassword(userForm.getPassword());
        User savedUser = userService.saveUser(user);
        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message", message);
        System.out.println(savedUser);
        return "redirect:/signup";
    }
}
