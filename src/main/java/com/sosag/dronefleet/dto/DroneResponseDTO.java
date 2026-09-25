package com.sosag.dronefleet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Drone API responses.
 *
 * <p>This record represents the data returned to the client when querying
 * drone information, hiding internal persistence details.</p>
 */
public record DroneResponseDTO(
        Long droneId,
        String serialNumber,
        Long droneModelId,
        String status,
        BigDecimal batteryLevel,
        BigDecimal totalFlightHours,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Maps a Drone entity to a DroneResponseDTO.
     *
     * @param drone the Drone entity to map
     * @return a new DroneResponseDTO instance
     */
    public static DroneResponseDTO fromEntity(com.sosag.dronefleet.model.Drone drone) {
        return new DroneResponseDTO(
                drone.getDroneId(),
                drone.getSerialNumber(),
                drone.getDroneModel().getDroneModelId(),
                drone.getStatus(),
                drone.getBatteryLevel(),
                drone.getTotalFlightHours(),
                drone.getCreatedAt(),
                drone.getUpdatedAt()
        );
    }
}