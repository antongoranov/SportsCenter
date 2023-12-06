package com.sportscenter.exception;


//TODO set status code
public class UnableToProcessBookingException extends RuntimeException {
    public UnableToProcessBookingException(String message) {
        super(message);
    }
}
