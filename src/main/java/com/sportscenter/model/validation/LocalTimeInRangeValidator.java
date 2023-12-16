package com.sportscenter.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;


public class LocalTimeInRangeValidator implements ConstraintValidator<LocalTimeInRange, LocalTime> {

    @Override
    public boolean isValid(LocalTime time, ConstraintValidatorContext context) {

        if(time == null) return false;

        //isAfter and isBefore are exclusive
        //current setting is or hours from 10:00 - 21:00
        return time.isAfter(LocalTime.of(9, 0)) &&
                time.isBefore(LocalTime.of(22, 0));
    }
}
