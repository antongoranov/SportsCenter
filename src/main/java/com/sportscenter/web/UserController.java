package com.sportscenter.web;

import com.sportscenter.model.binding.UserPictureBindingModel;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserRoleViewModel;
import com.sportscenter.model.view.UserViewModel;
import com.sportscenter.service.UserRoleService;
import com.sportscenter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;

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


    //*****ADMIN*****
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public String allUsers(Model model){

        List<UserViewModel> allUsers = userService.getAllUsers();

        model.addAttribute("allUsers", allUsers);

        return "users-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}/edit")
    public String editUser(@PathVariable("userId") Long userId,
                           Model model){

        UserViewModel existingUser = userService.   getUserById(userId);
        Set<UserRoleViewModel> roles = userRoleService.findAll();

        model.addAttribute("user", existingUser);
        model.addAttribute("allRoles", roles);

        return "user-edit-roles";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{userId}/roles")
    public String updateUserRoles(@PathVariable("userId") Long userId,
                                  @ModelAttribute("user") UserViewModel user) {

        userService.updateUserRoles(userId, user.getRoles());

        return "redirect:/users/all";
    }

    //DELETE USER
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long userId){

        userService.deleteUserById(userId);
        return "redirect:/users/all";
    }

}
