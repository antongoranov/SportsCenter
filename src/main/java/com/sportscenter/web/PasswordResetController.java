package com.sportscenter.web;

import com.sportscenter.model.binding.PasswordChangeBindingModel;
import com.sportscenter.model.binding.PasswordResetBindingModel;
import com.sportscenter.model.entity.PasswordResetToken;
import com.sportscenter.model.service.PasswordChangeServiceModel;
import com.sportscenter.service.PasswordResetTokenService;
import com.sportscenter.service.UserService;
import com.sportscenter.service.impl.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Controller
public class PasswordResetController {

    private final PasswordResetTokenService passwordResetTokenService;
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/resetPassword")
    public String getResetPassword(Model model) {

        if (!model.containsAttribute("pwResetBindingModel")) {
            model.addAttribute("pwResetBindingModel", new PasswordResetBindingModel());
        }

        return "user-reset-password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@Valid PasswordResetBindingModel pwResetBindingModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("pwResetBindingModel", pwResetBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.pwResetBindingModel", bindingResult);

            return "redirect:/resetPassword";
        }

        String userEmail = pwResetBindingModel.getEmail();
        if (!userService.userWithEmailExists(userEmail)) {
            redirectAttributes.addFlashAttribute("invalidEmail", true);

            return "redirect:/resetPassword";
        }


        PasswordResetToken pwResetToken;
        if(passwordResetTokenService.userHasExistingToken(userEmail)) {

            PasswordResetToken existingToken =
                    passwordResetTokenService.getTokenByUserEmail(userEmail);

            if(passwordResetTokenService.isTokenValid(existingToken.getToken())){
                pwResetToken = existingToken;
            } else {
                passwordResetTokenService.deleteToken(existingToken);
                pwResetToken =
                        passwordResetTokenService.createPasswordResetTokenForUser(userEmail);
            }
        } else {
            pwResetToken =
                    passwordResetTokenService.createPasswordResetTokenForUser(userEmail);
        }

        String contextPath = ServletUriComponentsBuilder
                .fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        emailService.sendPasswordResetEmail(pwResetToken, contextPath);

        redirectAttributes.addFlashAttribute("emailSent", true);
        return "redirect:/resetPassword";
    }

    @GetMapping("/changePassword")
    public String getChangePassword(Model model, @RequestParam String token) {

        if (!(passwordResetTokenService.isTokenPresent(token) && passwordResetTokenService.isTokenValid(token))) {
            model.addAttribute("invalidToken", true);
            return "user-change-password";
        }

        if (!model.containsAttribute("pwChangeBindingModel")) {
            PasswordChangeBindingModel pwChangeBindingModel =
                    PasswordChangeBindingModel.builder()
                            .token(token)
                            .build();

            model.addAttribute("pwChangeBindingModel", pwChangeBindingModel);
        }


        return "user-change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid PasswordChangeBindingModel pwChangeBindingModel,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("pwChangeBindingModel", pwChangeBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.pwChangeBindingModel", bindingResult);

            return "redirect:/changePassword?token=" + pwChangeBindingModel.getToken();
        }

        if (!pwChangeBindingModel.getNewPassword().equals(pwChangeBindingModel.getConfirmPassword())) {

            redirectAttributes.addFlashAttribute("differentPasswords", true);
            redirectAttributes.addFlashAttribute("pwChangeBindingModel", pwChangeBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.pwChangeBindingModel", bindingResult);

            return "redirect:/changePassword?token=" + pwChangeBindingModel.getToken();
        }

        PasswordChangeServiceModel pwChangeServiceModel =
                PasswordChangeServiceModel.builder()
                        .token(pwChangeBindingModel.getToken())
                        .newPassword(pwChangeBindingModel.getNewPassword())
                        .build();

        userService.changeUserPassword(pwChangeServiceModel);

        redirectAttributes.addFlashAttribute("passwordChangeSuccessful", true);
        return "redirect:/users/login";
    }
}
