package com.sportscenter.service;

import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.model.view.SportClassViewModel;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public interface SportClassService {

    List<SportClassViewModel> allClasses();
    SportClassViewModel getMatchedSportClass(
            List<SportClassViewModel> sportClasses,
            int dayOfWeek,
            int startHour);


    SportClassBookingViewModel getSportClassById(Long sportClassId);

    boolean hasAvailableSpots(Long sportClassId);

    void updateCapacity(SportClassEntity sportClass);

    void decreaseCapacity(SportClassEntity bookedSportClass);

    void resetSportClassCapacityAtEndOfDay();
}
