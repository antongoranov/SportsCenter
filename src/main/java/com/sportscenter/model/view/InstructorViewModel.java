package com.sportscenter.model.view;

import lombok.Data;

import java.util.List;

@Data
public class InstructorViewModel {

    private Long id;
    private String fullName;
    private String bio;
    private String pictureUrl;
    private String sportName;

    private String email;
    private String phoneNumber;
    List<SportClassViewModel> sportClasses;

}
