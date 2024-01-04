package com.sportscenter.model.binding;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddSportBindingModel {

    @NotEmpty(message = "Sport name cannot be empty!")
    @Size(min = 3, max = 20, message = "Must be between 3 and 20 characters!")
    private String name;

    @NotEmpty(message = "Description cannot be empty!")
    private String description;

    @NotEmpty(message = "Sport image url cannot be empty!")
    private String sportImageUrl;

}
