package com.sosag.dronefleet.exception;

/**
 * Exception thrown when a requested drone cannot be found.
 */
public class DroneNotFoundException extends RuntimeException {

    public DroneNotFoundException(String message) {
        super(message);
    }
}