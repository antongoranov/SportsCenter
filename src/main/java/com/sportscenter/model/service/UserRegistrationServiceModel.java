package com.sportscenter.model.service;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class UserRegistrationServiceModel {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

}
