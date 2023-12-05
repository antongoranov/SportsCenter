package com.sportscenter.service;

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


}