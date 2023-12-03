package com.sportscenter.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/login-error")
    public String loginError(@ModelAttribute("username") String username,
                             Model model) {

        model.addAttribute("username", username);
        model.addAttribute("loginFailed", true);

        return "login";
    }
}
