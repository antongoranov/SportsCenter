package com.sportscenter.model.service;


import lombok.Data;

@Data
public class UserRegistrationServiceModel {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

}
