package com.sportscenter.service.impl;

import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.enums.BookingStatusEnum;
import com.sportscenter.repository.BookingRepository;
import com.sportscenter.repository.SportClassRepository;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.SportClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SportClassRepository sportClassRepository;
    private final SportClassService sportClassService;

    @Override
    public void bookASportClass(UserDetails userDetails, Long sportClassId) {

        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        //move the check to the controller - do not call the method if invalid
//        boolean hasActiveBookings = findActiveBookingsByUser(user);
//
//
//        if(hasActiveBookings) {
//            //LOGGER
//            throw new UnableToProcessBookingException("Unable to process booking! " + user.getUsername() + " is having active bookings!");
//        }

        //will always have a valid sportClass id, as it passed from the bookSportClass GET endpoint
        SportClassEntity sportClass = sportClassRepository.findById(sportClassId).get();

        //move the check to the controller - do not call the method if invalid
//        boolean hasAvailableSportClassSpots = sportClassService.hasAvailableSpots(sportClass.getId());
//
//        if(!hasAvailableSportClassSpots) {
//            //LOGGER
//            throw new UnableToProcessBookingException("Unable to process booking! " + sportClass.getSportClassInfo() + " does not have available spots!");
//        }

        BookingEntity booking = BookingEntity.builder()
                .sportClass(sportClass)
                .user(user)
                .status(BookingStatusEnum.ACTIVE)
                .build();

        //update the current sportClass with the new capacity
        sportClassService.updateCapacity(sportClass);

        //save booking to db
        bookingRepository.save(booking);

    }

    @Override
    public boolean findActiveBookingsByUser(UserDetails userDetails) {

        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        return bookingRepository.findAllByUserAndStatus(user, BookingStatusEnum.ACTIVE)
                .size() > 0;
    }
}
