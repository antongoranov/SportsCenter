package com.sportscenter.web;

import com.sportscenter.model.binding.UserPictureBindingModel;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users/")
public class UserProfileController {

    private final UserService userService;

    @ModelAttribute
    public UserPictureBindingModel userPictureBindingModel() {
        return new UserPictureBindingModel();
    }

    @GetMapping("/myProfile")
    public String getMyProfile(Principal principal,
                               Model model) {

        UserProfileViewModel user = userService.getUserProfileByUsername(principal.getName());

        model.addAttribute("user", user);

        return "profile";
    }

    @PatchMapping("/myProfile/uploadProfilePic")
    public String changeProfilePic(Principal principal,
                                   UserPictureBindingModel userPictureBindingModel) throws IOException {

        UserPictureServiceModel userPictureServiceModel =
                UserPictureServiceModel.builder()
                        .username(principal.getName())
                        .filePicture(userPictureBindingModel.getFilePicture())
                .build();

        userService.changeProfilePic(userPictureServiceModel);

        return "redirect:/users/myProfile";
    }

}
