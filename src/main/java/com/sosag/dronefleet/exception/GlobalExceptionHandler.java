package com.sosag.dronefleet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Global exception handler for the REST API.
 *
 * <p>Centralizes error handling to ensure consistent RFC 7807 Problem Detail
 * responses across all controllers.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DroneNotFoundException and returns a 404 Not Found ProblemDetail.
     *
     * @param ex the thrown DroneNotFoundException
     * @return a ProblemDetail response
     */
    @ExceptionHandler(DroneNotFoundException.class)
    public ProblemDetail handleDroneNotFoundException(DroneNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Drone Not Found");
        problemDetail.setType(URI.create("https://api.dronefleet.com/errors/drone-not-found"));
        return problemDetail;
    }

    /**
     * Handles validation errors (e.g., @Valid failures) and returns a 400 Bad Request ProblemDetail.
     *
     * @param ex the thrown MethodArgumentNotValidException
     * @return a ProblemDetail response with validation details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://api.dronefleet.com/errors/validation-error"));

        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        problemDetail.setProperty("validationErrors", errors);
        return problemDetail;
    }

    /**
     * Handles generic IllegalArgumentExceptions (e.g., Drone Model not found).
     *
     * @param ex the thrown IllegalArgumentException
     * @return a ProblemDetail response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("https://api.dronefleet.com/errors/bad-request"));
        return problemDetail;
    }
}