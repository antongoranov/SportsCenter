package com.sportscenter.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalTimeInRangeValidator.class)
public @interface LocalTimeInRange {

    String message() default "Time is not in the specified range!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
