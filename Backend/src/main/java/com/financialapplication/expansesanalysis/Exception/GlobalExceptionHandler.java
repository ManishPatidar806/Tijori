package com.financialapplication.expansesanalysis.Exception;

import com.financialapplication.expansesanalysis.Model.Response.ErrorResponse;
import com.financialapplication.expansesanalysis.Model.Response.ValidateErrorResponse;
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
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = Logger.getLogger(getClass().getName());

    // Utility method to create ErrorResponse
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, boolean status, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setStatus(status);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        logger.warning("NotFoundException: " + exception.getMessage());
        return buildErrorResponse(
                exception.getMessage().isEmpty() ? "Resource Not Found" : exception.getMessage(),
                false,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException exception) {
        logger.warning("UserAlreadyExistException: " + exception.getMessage());
        return buildErrorResponse("User already exists.", false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException exception) {
        logger.warning("FileNotFoundException: " + exception.getMessage());
        return buildErrorResponse("The requested file could not be found on the server.", false, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException exception) {
        logger.warning("SQLException: " + exception.getMessage());
        return buildErrorResponse("A database error occurred while processing the request.", false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException exception) {
        logger.warning("CommonException: " + exception.getMessage());
        return buildErrorResponse(exception.getMessage(), false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidateErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logger.warning("Validation Error: " + exception.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError err : exception.getBindingResult().getFieldErrors()) {
            errors.put(err.getField(), err.getDefaultMessage());
        }
        ValidateErrorResponse response = new ValidateErrorResponse();
        response.setMessage(errors);
        response.setStatus(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnAuthorizeException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorizeException(UnAuthorizeException exception) {
        logger.warning("UnAuthorizeException: " + exception.getMessage());
        return buildErrorResponse(
                "Unauthorized access detected. Please ensure you have the necessary permissions to perform this action.",
                false,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.warning("IllegalArgumentException: " + exception.getMessage());
        return buildErrorResponse("Invalid argument provided: " + exception.getMessage(), false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        logger.severe("Unexpected Exception: " + exception.getMessage());
        return buildErrorResponse(
                "An unexpected error occurred while processing your request. Please try again later or contact support if the issue persists.",
                false,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}