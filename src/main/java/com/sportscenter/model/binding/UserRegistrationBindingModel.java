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

    @NotEmpty(message = "Enter a valid first name!")
    @Size(min = 2, max = 20)
    private String firstName;

    @NotEmpty(message = "Enter a valid last name!")
    @Size(min = 2, max = 20)
    private String lastName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Enter a valid email!")
    @UniqueEmail(message = "Email exists!")
    private String email;

    @NotEmpty(message = "Enter a username")
    @UniqueUsername(message = "Username exists!")
    private String username;

    @NotEmpty
    @Size(min = 5)
    private String password;

    @NotEmpty
    @Size(min = 5)
    private String confirmPassword;


}
