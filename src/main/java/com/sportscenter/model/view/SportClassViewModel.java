package com.sportscenter.model.view;

import com.sportscenter.model.entity.SportEntity;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class SportClassViewModel {

    private Long id;
    private String sportName;
    private String instructorName;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;


    //old - for test purposes
    //private String dayOfWeek;
    //private int startHour;
    //private int endHour;

}
