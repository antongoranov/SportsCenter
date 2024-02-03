package com.sportscenter.model.binding;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeBindingModel {

    private String token;

    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 5, message = "Must be at least 5 characters!")
    private String newPassword;

    @NotEmpty(message = "Confirm password cannot be empty!")
    @Size(min = 5, message = "Must be at least 5 characters!")
    private String confirmPassword;

}
