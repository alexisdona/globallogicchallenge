package com.globallogic.challenge.exception;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {
    private final HttpStatus errorCode;

    public APIException(HttpStatus errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
