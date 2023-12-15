package com.sportscenter.model.service;
import lombok.Data;

@Data
public class AddInstructorServiceModel {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long sportId;
    private String pictureUrl;
    private String bio;
}
