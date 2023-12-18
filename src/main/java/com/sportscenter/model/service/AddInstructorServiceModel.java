package com.sportscenter.model.service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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
