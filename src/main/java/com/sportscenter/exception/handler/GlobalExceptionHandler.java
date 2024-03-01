package com.sportscenter.exception.handler;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.exception.QrCodeException;
import com.sportscenter.exception.UnableToProcessOperationException;
import com.sportscenter.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ObjectNotFoundException.class, UserNotFoundException.class})
    public String handleObjectNotFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return "error/404";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class, UnableToProcessOperationException.class})
    public String handleUnableToProcessOperationException(Exception e) {
        log.error(e.getMessage(), e);
        return "error/403";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, QrCodeException.class})
    public String handleInternalServerError(Exception e) {
        log.error(e.getMessage(), e);
        return "error/500";
    }
}
