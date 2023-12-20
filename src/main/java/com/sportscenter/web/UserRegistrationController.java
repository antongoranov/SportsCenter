package com.sportscenter.web;

import com.sportscenter.model.binding.UserRegistrationBindingModel;
import com.sportscenter.model.mapper.UserMapper;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserRegistrationController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/register")
    public String register(Model model){

        if(!model.containsAttribute("userRegistrationModel")) {
            model.addAttribute("userRegistrationModel", new UserRegistrationBindingModel());
        }

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid UserRegistrationBindingModel userRegistrationBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userRegistrationModel", userRegistrationBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userRegistrationModel", bindingResult);

            return "redirect:/users/register";
        }

        if(!userRegistrationBindingModel.getPassword().equals(userRegistrationBindingModel.getConfirmPassword())){

            redirectAttributes.addFlashAttribute("differentPasswords", true);
            redirectAttributes.addFlashAttribute("userRegistrationModel", userRegistrationBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userRegistrationModel", bindingResult);

            return "redirect:/users/register";
        }

        UserRegistrationServiceModel userRegistrationServiceModel =
                userMapper.userRegistrationBindingToUserRegistrationServiceModel(userRegistrationBindingModel);

        userService.register(userRegistrationServiceModel);

        redirectAttributes.addFlashAttribute("registrationSuccess", true);

        return "redirect:/users/login";
    }
}
