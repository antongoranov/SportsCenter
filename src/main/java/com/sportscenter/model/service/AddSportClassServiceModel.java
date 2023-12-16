package com.sportscenter.model.service;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class AddSportClassServiceModel {

    private Long instructorId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxCapacity;

}
