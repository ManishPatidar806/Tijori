package com.financialapplication.tijori.admin.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (exception instanceof IOException ioException) {
                String message = ioException.getMessage();
                if (message != null && message.contains("Broken pipe")) {
                    logger.debug("Client disconnected (broken pipe) - this is normal for SSE streams");
                    return "forward:/";
                }
            }

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.debug("Resource not found: {}", request.getRequestURI());
                return "forward:/";
            } else if (statusCode >= 500) {
                logger.error("Server error occurred: {} for URI: {}", statusCode, request.getRequestURI());
            }
        }

        return "forward:/";
    }
}


