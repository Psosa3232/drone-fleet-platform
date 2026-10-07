package com.sosag.dronefleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main entry point for the Drone Fleet Platform application.
 * Enables caching support for Redis-based performance optimization.
 */
@SpringBootApplication
@EnableCaching
public class DroneFleetApplication {

    /**
     * Main method that launches the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DroneFleetApplication.class, args);
    }
}