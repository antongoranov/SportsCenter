package com.sportscenter.service;

import org.springframework.security.core.userdetails.UserDetails;

//TODO
public interface BookingService {

    void bookASportClass(UserDetails userDetails, Long sportClassId);
}
