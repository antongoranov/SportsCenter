package com.sportscenter.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserLoginController {

    @GetMapping("/login")
    public String login(Model model) {

        if(model.containsAttribute("registrationSuccess")){
            model.addAttribute("registrationSuccessAttr", true);
        }

        return "login";
    }
}
