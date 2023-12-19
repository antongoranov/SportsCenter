package com.sportscenter.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileViewModel {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String profilePictureUrl;

    private List<BookingViewModel> bookings;

}
