package com.sportscenter.service;

import com.sportscenter.model.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

//TODO
public interface BookingService {

    void bookASportClass(UserDetails userDetails, Long sportClassId);

    boolean findActiveBookingsByUser(UserDetails userDetails);
}
