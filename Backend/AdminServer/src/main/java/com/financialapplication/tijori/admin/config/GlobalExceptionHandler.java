package com.financialapplication.tijori.admin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException ex) {
        String message = ex.getMessage();

        if (message != null && (message.contains("Broken pipe") ||
                               message.contains("Connection reset") ||
                               message.contains("Broken channel"))) {
            logger.debug("Client disconnected during streaming: {}", message);
        } else {
            logger.warn("IOException occurred: {}", message, ex);
        }
    }
}


