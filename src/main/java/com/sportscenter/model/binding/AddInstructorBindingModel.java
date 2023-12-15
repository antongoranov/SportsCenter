package com.sportscenter.model.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddInstructorBindingModel {

    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 2, max = 20, message = "Must be between 2 and 20 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 20, message = "Must be between 2 and 20 characters!")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Enter a valid email!")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty!")
    @Size(min = 10, max = 13, message = "Phone number must start with + and be from 10 to 13 digits!")
    private String phoneNumber;

    @NotNull(message = "Please select a Sport!")
    private Long sportId;

    @NotEmpty(message = "Picture url cannot be empty!")
    private String pictureUrl;

    @NotEmpty(message = "Biography cannot be empty!")
    private String bio;


}
