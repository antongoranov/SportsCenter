package com.sportscenter.service;

import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.service.UserSearchServiceModel;
import com.sportscenter.model.view.BookingViewModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface BookingService {

    void bookASportClass(UserDetails userDetails, Long sportClassId);

    boolean hasActiveBookings(UserDetails userDetails);

    List<BookingViewModel> findBookingsByUser(UserDetails userDetails);

    List<BookingViewModel> findBookingsByUsername(UserSearchServiceModel userSearchServiceModel);

    List<BookingEntity> getActiveBookingsByUser(UserEntity user);

    void cancelBooking(Long bookingId);

    void acceptBooking(Long bookingId);

    void expireBookingsAtEndOfDay();

    void deleteExpiredBookingsAtEndOfWeek();

//    boolean isUserIssuerOfBooking(UserDetails userDetails, Long bookingId);
}
