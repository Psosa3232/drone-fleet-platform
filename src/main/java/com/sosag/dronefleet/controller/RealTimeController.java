package com.sosag.dronefleet.controller;

import com.sosag.dronefleet.service.RealTimeDroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * REST controller for real-time drone telemetry endpoints.
 * Exposes endpoints that read from Redis for low-latency access
 * to the current state of each drone.
 */
@RestController
@RequestMapping("/api/realtime")
public class RealTimeController {

    private final RealTimeDroneService realTimeDroneService;

    /**
     * Constructs the RealTimeController with the provided service.
     *
     * @param realTimeDroneService the service for real-time drone state management
     */
    public RealTimeController(RealTimeDroneService realTimeDroneService) {
        this.realTimeDroneService = realTimeDroneService;
    }

    /**
     * Retrieves the current real-time state of a specific drone.
     *
     * @param serialNumber the serial number of the drone (e.g., "D001")
     * @return a ResponseEntity containing the drone's state as a map,
     *         or a 404 Not Found response if the drone is not tracked
     */
    @GetMapping("/drones/{serialNumber}")
    public ResponseEntity<Map<Object, Object>> getDroneLiveState(@PathVariable String serialNumber) {
        Map<Object, Object> state = realTimeDroneService.getDroneState(serialNumber);
        if (state.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(state);
    }
}