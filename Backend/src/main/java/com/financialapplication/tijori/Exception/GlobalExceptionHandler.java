package com.financialapplication.tijori.Exception;

import com.financialapplication.tijori.Model.Response.ApiResponse;
import com.financialapplication.tijori.Model.Response.ErrorResponse;
import com.financialapplication.tijori.Model.Response.ValidateErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String CORRELATION_ID = "correlationId";

    // Utility method to create ErrorResponse
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, boolean status, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setStatus(status);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    // New method using ApiResponse
    private <T> ResponseEntity<ApiResponse<T>> buildApiErrorResponse(
            String message, String errorCode, String details, HttpStatus httpStatus) {
        String correlationId = MDC.get(CORRELATION_ID);
        ApiResponse<T> response = ApiResponse.error(message, errorCode, 
                correlationId != null ? "Correlation ID: " + correlationId + ". " + details : details);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleRateLimitExceededException(RateLimitExceededException exception) {
        log.warn("RateLimitExceededException: {}", exception.getMessage());
        return buildApiErrorResponse(
                exception.getMessage(),
                "RATE_LIMIT_EXCEEDED",
                "Please wait before making more requests.",
                HttpStatus.TOO_MANY_REQUESTS
        );
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        log.warn("NotFoundException: {}", exception.getMessage());
        return buildErrorResponse(
                exception.getMessage().isEmpty() ? "Resource Not Found" : exception.getMessage(),
                false,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException exception) {
        log.warn("UserAlreadyExistException: {}", exception.getMessage());
        return buildErrorResponse("User already exists.", false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException exception) {
        log.warn("FileNotFoundException: {}", exception.getMessage());
        return buildErrorResponse("The requested file could not be found on the server.", false, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException exception) {
        log.warn("SQLException: {}", exception.getMessage());
        return buildErrorResponse("A database error occurred while processing the request.", false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        log.warn("BusinessException: {}", exception.getMessage());
        return buildErrorResponse(exception.getMessage(), false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidateErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("Validation Error: {}", exception.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError err : exception.getBindingResult().getFieldErrors()) {
            errors.put(err.getField(), err.getDefaultMessage());
        }
        ValidateErrorResponse response = new ValidateErrorResponse();
        response.setMessage(errors);
        response.setStatus(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
        log.warn("UnauthorizedException: {}", exception.getMessage());
        return buildErrorResponse(
                "Unauthorized access detected. Please ensure you have the necessary permissions to perform this action.",
                false,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.warn("IllegalArgumentException: {}", exception.getMessage());
        return buildErrorResponse("Invalid argument provided: " + exception.getMessage(), false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        log.error("Unexpected Exception: {}", exception.getMessage(), exception);
        return buildErrorResponse(
                "An unexpected error occurred while processing your request. Please try again later or contact support if the issue persists.",
                false,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
