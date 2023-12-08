package com.sportscenter.service;

import com.sportscenter.model.view.BookingViewModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

//TODO
public interface BookingService {

    void bookASportClass(UserDetails userDetails, Long sportClassId);

    boolean hasActiveBookings(UserDetails userDetails);

    List<BookingViewModel> findBookingsByUser(UserDetails userDetails);

    void cancelBooking(UserDetails userDetails, Long bookingId);

    boolean isUserIssuerOfBooking(UserDetails userDetails, Long bookingId);
}
