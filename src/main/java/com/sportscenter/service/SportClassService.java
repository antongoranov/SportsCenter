package com.sportscenter.service;

import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.model.view.SportClassViewModel;

import java.time.DayOfWeek;
import java.util.List;

public interface SportClassService {

    List<SportClassViewModel> allClasses();


    //remove it
    List<SportClassViewModel> sportClassesByDayOfWeek(DayOfWeek day);

    SportClassViewModel getMatchedSportClass(
            List<SportClassViewModel> sportClasses,
            int dayOfWeek,
            int startHour);


    SportClassBookingViewModel getSportClassById(Long sportClassId);

    boolean hasAvailableSpots(SportClassEntity sportClass);

    void updateCapacity(SportClassEntity sportClass);
}
