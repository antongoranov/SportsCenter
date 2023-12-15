package com.sportscenter.model.binding;

import com.sportscenter.model.validation.UniqueEmail;
import com.sportscenter.model.validation.UniqueUsername;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationBindingModel {

    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 2, max = 20, message = "Must be between 2 and 20 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 20, message = "Must be between 2 and 20 characters!")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Enter a valid email!")
    @UniqueEmail(message = "Email exists!")
    private String email;

    @NotEmpty(message = "Username cannot be empty!")
    @UniqueUsername(message = "Username exists!")
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 5, message = "Must be at least 5 characters!")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty!")
    @Size(min = 5, message = "Must be at least 5 characters!")
    private String confirmPassword;


}
