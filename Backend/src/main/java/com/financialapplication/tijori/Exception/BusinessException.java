package com.financialapplication.tijori.Exception;

import lombok.Getter;

/**
 * General application exception for common error scenarios.
 * Can be used for validation errors and business logic violations.
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final int statusCode;

    public BusinessException(String message) {
        super(message);
        this.statusCode = 400; // Default to Bad Request
    }

    public BusinessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}