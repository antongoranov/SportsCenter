package com.sportscenter.service;

import com.sportscenter.model.service.UserSearchServiceModel;
import com.sportscenter.model.view.BookingViewModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface BookingService {

    void bookASportClass(UserDetails userDetails, Long sportClassId);

    boolean hasActiveBookings(UserDetails userDetails);

    List<BookingViewModel> findBookingsByUser(UserDetails userDetails);

    void cancelBooking(Long bookingId);

    List<BookingViewModel> findBookingsByUsername(UserSearchServiceModel userSearchServiceModel);

    void acceptBooking(Long bookingId);

    //Everyday at midnight set the status of unused bookings to expired
    @Scheduled(cron = "0 0 0 * * ?")
    void expireBookingsAtEndOfDay();

    void deleteExpiredBookingsAtEndOfWeek();

//    boolean isUserIssuerOfBooking(UserDetails userDetails, Long bookingId);
}
