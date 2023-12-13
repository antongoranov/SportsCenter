package com.sportscenter.model.view;

import com.sportscenter.model.entity.SportEntity;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Data
public class SportClassViewModel {

    private Long id;
    private String sportName;
    private String instructorName;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public String getSportClassInfo(){
        return String.format("%s with %s - %s %s to %s",
                sportName,
                instructorName,
                dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

}
