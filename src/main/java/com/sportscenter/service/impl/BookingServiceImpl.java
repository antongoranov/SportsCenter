package com.sportscenter.service.impl;

import com.sportscenter.exception.UserNotFoundException;
import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.enums.BookingStatusEnum;
import com.sportscenter.model.mapper.BookingMapper;
import com.sportscenter.model.service.UserSearchServiceModel;
import com.sportscenter.model.view.BookingViewModel;
import com.sportscenter.repository.BookingRepository;
import com.sportscenter.repository.SportClassRepository;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.SportClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SportClassRepository sportClassRepository;
    private final SportClassService sportClassService;
    private final BookingMapper bookingMapper;

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
    public boolean hasActiveBookings(UserDetails userDetails) {

        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        return bookingRepository.findAllByUserAndStatus(user, BookingStatusEnum.ACTIVE)
                .size() > 0;
    }

    @Override
    public List<BookingViewModel> findBookingsByUser(UserDetails userDetails) {

        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User with " + userDetails.getUsername() + " does not exist!"));

        return bookingRepository.findAllByUser(user)
                .stream()
                .map(bookingMapper::bookingEntityToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(Long bookingId) {

//        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow();

        //the logged user can only see his bookings, hence, there is no need to check if the user is an owner
        //will always have a valid booking id
        BookingEntity booking = bookingRepository.findById(bookingId).get();

        //handled by isUserIssuerOfBooking(UserDetails userDetails, Long bookingId)
        //if(user.getId().equals(booking.getUser().getId())){}

        booking.setStatus(BookingStatusEnum.CANCELLED);

        SportClassEntity bookedSportClass = booking.getSportClass();
        sportClassService.decreaseCapacity(bookedSportClass);

        bookingRepository.save(booking);
    }

    @Override
    public List<BookingViewModel> findBookingsByUsername(UserSearchServiceModel userSearchServiceModel) {

        String username = userSearchServiceModel.getUsername();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with " + username + " does not exist!"));

        return bookingRepository.findAllByUser(user)
                .stream()
                .map(bookingMapper::bookingEntityToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public void acceptBooking(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId).get();

        booking.setStatus(BookingStatusEnum.ACCEPTED);

        bookingRepository.save(booking);
    }

    //***Scheduled tasks***

    //Everyday at midnight set the status of unused bookings to expired
    @Override
    @Scheduled(cron = "0 59 23 * * ?")
    public void expireBookingsAtEndOfDay() {
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();

        List<BookingEntity> bookingsToExpire =
                bookingRepository.findBySportClassDayOfWeekAndStatus(today, BookingStatusEnum.ACTIVE);

        bookingsToExpire
                .forEach(booking -> {
                    booking.setStatus(BookingStatusEnum.EXPIRED);
                    bookingRepository.save(booking);
                });
    }


    //Every Sunday at 23:59 delete all non Active bookings to reset them for the next week;
    //Leave the Active ones which are already made for the next week.
    @Override
    @Scheduled(cron = "0 59 23 ? * SUN")
    public void deleteExpiredBookingsAtEndOfWeek() {

        List<BookingEntity> notActiveBookings =
                bookingRepository.findByStatusNot(BookingStatusEnum.ACTIVE);

        bookingRepository.deleteAll(notActiveBookings);
    }


    //the logged user can only see his bookings, hence, there is no need to check if the user is an owner
//    @Override
//    public boolean isUserIssuerOfBooking(UserDetails userDetails, Long bookingId) {
//
//        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow();
//
//        BookingEntity booking = bookingRepository.findById(bookingId).get();
//
//        return user.getId().equals(booking.getUser().getId());
//    }
}
