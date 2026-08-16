package com.trackwise.common.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.trackwise.com/errors/not-found"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(
            BusinessException ex, WebRequest request) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("https://api.trackwise.com/errors/bad-request"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(
                        status, "Validation failed for one or more fields");
        problemDetail.setTitle("Constraint Violation");
        problemDetail.setType(URI.create("https://api.trackwise.com/errors/validation-failed"));

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAllUncaughtExceptions(
            Exception ex, WebRequest request) {
        // Log the exception details in real-world apps
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred. Please try again later.");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.trackwise.com/errors/internal-server-error"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
