package com.sportscenter.service.impl;

import com.sportscenter.exception.UnableToProcessBookingException;
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

        boolean hasActiveBookings = findActiveBookingsByUser(user);

        if(hasActiveBookings) {
            //LOGGER
            throw new UnableToProcessBookingException("Unable to process booking! " + user.getUsername() + " is having active bookings!");
        }

        //will always have a valid sportClass id, as it passed from the bookSportClass GET endpoint
        SportClassEntity sportClass = sportClassRepository.findById(sportClassId).get();

        boolean hasAvailableSportClassSpots = sportClassService.hasAvailableSpots(sportClass);

        if(!hasAvailableSportClassSpots) {
            //LOGGER
            throw new UnableToProcessBookingException("Unable to process booking! " + sportClass.getSportClassInfo() + " does not have available spots!");
        }

        BookingEntity booking = BookingEntity.builder()
                .sportClass(sportClass)
                .user(user)
                .status(BookingStatusEnum.ACTIVE)
                .build();

        //sportClass.setCurrentCapacity(sportClass.getCurrentCapacity() + 1);
        //update the current sportClass with the new capacity and save to db
        sportClassService.updateCapacity(sportClass);
        bookingRepository.save(booking);

    }

    private boolean findActiveBookingsByUser(UserEntity user) {
        return bookingRepository.findAllByUserAndStatus(user, BookingStatusEnum.ACTIVE)
                .size() > 0;
    }
}
