package com.sportscenter.model.view;

import com.sportscenter.model.entity.SportEntity;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Data
public class SportClassBookingViewModel {

    //sportClassId
    private Long id;

    //private SportEntity sport;
    private Long sportId;
    private String sportName;
    private String sportImageUrl;
    private String sportDescription;

    private String instructorName;

    private int maxCapacity;
    private int currentCapacity;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public String dayOfWeekGetDisplayName(){
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }
}
