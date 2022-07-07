package com.rest.ratelimitservice.exception;


import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleException(HttpServletRequest request, Exception ex) {
        if (ex instanceof RateLimitException) {
            log.warn(ex.getMessage(), ex);
            RateLimitException rateLimitException = (RateLimitException) ex;
            return ExceptionResponse.create(rateLimitException.getCode(), rateLimitException.getMessage());
        } else {
            log.error(ex.getMessage(), ex);
            return ExceptionResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
    }

}
