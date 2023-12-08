package com.sportscenter.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnableToProcessOperationException extends RuntimeException {
    public UnableToProcessOperationException(String message) {
        super(message);
    }
}
