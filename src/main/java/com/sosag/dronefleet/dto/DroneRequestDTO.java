package com.sosag.dronefleet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating a new Drone.
 *
 * <p>This record encapsulates the required data to register a new drone
 * in the system, excluding the serial number which is generated automatically.</p>
 */
public record DroneRequestDTO(

        @NotNull(message = "Drone model ID is required")
        Long droneModelId,

        @NotBlank(message = "Status is required")
        String status,

        @NotNull(message = "Battery level is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Battery level must be at least 0.0")
        BigDecimal batteryLevel,

        @NotNull(message = "Total flight hours is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Total flight hours must be at least 0.0")
        BigDecimal totalFlightHours
) {
}