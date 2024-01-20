package com.sportscenter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class QrCodeException extends RuntimeException {
    public QrCodeException(String msg) {
        super(msg);
    }
}
