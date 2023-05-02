package com.globallogic.challenge.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int code;
    private String detail;

}
