package com.rest.ratelimitservice.exception;


import com.rest.ratelimitservice.constant.RateLimitExceptionConstants;

public class RateLimitException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Integer code;

    public RateLimitException() {
        super(RateLimitExceptionConstants.ERR_MSG);
        this.code = RateLimitExceptionConstants.ERR_CODE;
    }

    public Integer getCode() {
        return code;
    }
}
