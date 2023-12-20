package com.sportscenter.web;

import com.sportscenter.model.binding.UserEditBindingModel;
import com.sportscenter.model.binding.UserPictureBindingModel;
import com.sportscenter.model.binding.UserRegistrationBindingModel;
import com.sportscenter.model.mapper.UserMapper;
import com.sportscenter.model.service.UserEditServiceModel;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserRoleViewModel;
import com.sportscenter.model.view.UserViewModel;
import com.sportscenter.service.UserRoleService;
import com.sportscenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final UserMapper userMapper;

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

    @PreAuthorize("@userServiceImpl.isLoggedUserTheAccountHolder(#principal.name, #userId)")
    @GetMapping("/edit/{userId}")
    public String getEditUser(Principal principal,
                              @PathVariable("userId") Long userId,
                              Model model) {

        if(!model.containsAttribute("user")) {
            UserViewModel existingUser = userService.getUserById(userId);
            UserEditBindingModel userEditBindingModel = userMapper.mapUserViewToBindingModel(existingUser);

            model.addAttribute("user", userEditBindingModel);
        }
        return "user-edit";
    }

    @PutMapping("/edit/{userId}")
    public String editUser(@PathVariable("userId") Long userId,
                           @Valid UserEditBindingModel userEditBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", userEditBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.user", bindingResult);

            return "redirect:/users/edit/" + userEditBindingModel.getId();
        }

        boolean isNewEmailDifferentAndExisting =
                userService.isNewEmailDifferentAndExisting(userId, userEditBindingModel.getEmail());

        boolean isNewUsernameDifferentAndExisting =
                userService.isNewUsernameDifferentAndExisting(userId, userEditBindingModel.getUsername());

        if(isNewEmailDifferentAndExisting || isNewUsernameDifferentAndExisting){
            redirectAttributes.addFlashAttribute("user", userEditBindingModel);
            redirectAttributes.addFlashAttribute("newEmailOrUsernameExisting", true);
            return "redirect:/users/edit/" + userEditBindingModel.getId();
        }

        UserEditServiceModel userEditServiceModel =
                userMapper.userEditBindingModelToService(userEditBindingModel);

        userService.editUserData(userId, userEditServiceModel);

        //force logout to login with the new username
        SecurityContextHolder.clearContext();
        if(session != null)
            session.invalidate();

        return "redirect:/users/login";
    }


    //*****ADMIN*****
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public String allUsers(Model model) {

        List<UserViewModel> allUsers = userService.getAllUsers();

        model.addAttribute("allUsers", allUsers);

        return "users-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}/edit")
    public String editUserRoles(@PathVariable("userId") Long userId,
                                Model model) {

        UserViewModel existingUser = userService.getUserById(userId);
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long userId) {

        userService.deleteUserById(userId);
        return "redirect:/users/all";
    }

}
