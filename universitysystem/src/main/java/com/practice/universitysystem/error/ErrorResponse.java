package com.practice.universitysystem.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private HttpStatus status;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String errorName;
    private String message;

}
