package com.sportscenter.model.binding;

import com.sportscenter.model.validation.LocalTimeInRange;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class AddSportClassBindingModel {

    @NotNull(message = "Please select an Instructor!")
    private Long instructorId;

    @NotNull(message = "Please select a Day of week")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Please select a start time!")
    @LocalTimeInRange(message = "Time should be between 10 AM and 20 PM!")
    private LocalTime startTime;

    @NotNull(message = "Please select an end time!")
    @LocalTimeInRange(message = "Time should be between 10 AM and 20 PM!")
    private LocalTime endTime;

    @Positive
    @NotNull(message = "Please enter maximum capacity!")
    @Max(value = 40, message = "Maximum capacity should be up to 40 people!")
    private Integer maxCapacity;

}
