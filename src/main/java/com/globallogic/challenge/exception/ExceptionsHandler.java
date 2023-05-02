package com.globallogic.challenge.exception;

import com.globallogic.challenge.controller.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<List<ErrorResponse>> handleException(APIException exception) {
        HttpStatus errorCode = exception.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.value())
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(Collections.singletonList(errorResponse), errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<ErrorResponse>> handleException(Exception exception) {
        HttpStatus errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.value())
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(Collections.singletonList(errorResponse), errorCode);
    }
}
