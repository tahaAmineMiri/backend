package com.incon.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ApiResponse {
    // Getters and Setters
    private String message;
    private HttpStatus status;

    // Constructor
    public ApiResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}