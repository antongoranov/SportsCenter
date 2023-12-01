package com.sportscenter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserRegistrationController {


    @GetMapping("/register")
    public String register(){
        //TODO
        return "register";
    }
}
