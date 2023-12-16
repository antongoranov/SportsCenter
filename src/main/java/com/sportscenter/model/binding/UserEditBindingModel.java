package com.sportscenter.model.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditBindingModel {

    private Long id;

    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 2, max = 20, message = "Must be between 2 and 20 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 20, message = "Must be between 2 and 20 characters!")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Enter a valid email!")
    private String email;

    @NotEmpty(message = "Username cannot be empty!")
    private String username;
}
