package com.sportscenter.model.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordResetBindingModel {

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Enter a valid email!")
    private String email;
}
