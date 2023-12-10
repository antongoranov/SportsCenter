package com.sportscenter.web;

import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users/")
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/myProfile")
    public String getMyProfile(Principal principal,
                               Model model){

        UserProfileViewModel user = userService.getUserProfileByUsername(principal.getName());

        model.addAttribute("user", user);

        return "profile";
    }

}
