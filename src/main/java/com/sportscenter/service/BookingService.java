package com.sportscenter.service;

import com.sportscenter.model.view.BookingViewModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

//TODO
public interface BookingService {

    void bookASportClass(UserDetails userDetails, Long sportClassId);

    boolean hasActiveBookings(UserDetails userDetails);

    List<BookingViewModel> findBookingsByUser(UserDetails userDetails);
}
