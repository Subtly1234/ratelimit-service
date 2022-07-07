package com.rest.ratelimitservice.exception;


public class ExceptionResponse {
    private final String message;
    private final Integer code;

    public ExceptionResponse(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public static ExceptionResponse create(Integer code, String message) {
        return new ExceptionResponse(code, message);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
