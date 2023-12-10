package com.sportscenter.model.view;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileViewModel {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String profilePictureUrl;

    private List<BookingViewModel> bookings;

}
