package com.financialapplication.tijori.Exception;

/**
 * Exception thrown when a requested resource is not found.
 * Extends RuntimeException for cleaner code without checked exception handling.
 */
public class NotFoundException extends RuntimeException {
    
    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}