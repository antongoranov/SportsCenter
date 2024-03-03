package com.sportscenter.model.view;

import com.sportscenter.model.entity.SportEntity;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Builder
@AllArgsConstructor
@Data
public class SportClassBookingViewModel {

    private Long id;
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
