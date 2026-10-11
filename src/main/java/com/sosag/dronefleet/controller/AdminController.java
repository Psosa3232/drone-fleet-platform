package com.sosag.dronefleet.controller;

import com.sosag.dronefleet.dto.DroneRequestDTO;
import com.sosag.dronefleet.dto.DroneResponseDTO;
import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import com.sosag.dronefleet.model.Maintenance;
import com.sosag.dronefleet.model.User;
import com.sosag.dronefleet.repository.DroneModelRepository;
import com.sosag.dronefleet.repository.MaintenanceRepository;
import com.sosag.dronefleet.repository.UserRepository;
import com.sosag.dronefleet.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST controller for Admin-only operations.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Operations", description = "Restricted endpoints for fleet administrators")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final DroneService droneService;
    private final DroneModelRepository droneModelRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;

    public AdminController(DroneService droneService,
                           DroneModelRepository droneModelRepository,
                           MaintenanceRepository maintenanceRepository,
                           UserRepository userRepository) {
        this.droneService = droneService;
        this.droneModelRepository = droneModelRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds a new drone to the fleet.
     */
    @PostMapping("/drones")
    @Operation(summary = "Add a new drone", description = "Registers a new drone in the fleet (Admin only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Drone created"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    public ResponseEntity<DroneResponseDTO> addDrone(@Valid @RequestBody DroneRequestDTO request) {
        DroneModel droneModel = droneModelRepository.findById(request.droneModelId())
                .orElseThrow(() -> new IllegalArgumentException("Drone model not found with id: " + request.droneModelId()));

        Drone newDrone = new Drone();
        newDrone.setDroneModel(droneModel);
        newDrone.setStatus(request.status());
        newDrone.setBatteryLevel(request.batteryLevel());
        newDrone.setTotalFlightHours(request.totalFlightHours());

        Drone savedDrone = droneService.createDrone(newDrone);
        return ResponseEntity.status(HttpStatus.CREATED).body(DroneResponseDTO.fromEntity(savedDrone));
    }

    /**
     * Sends a specific drone to maintenance status and creates a maintenance record.
     */
    @PatchMapping("/drones/{id}/send-to-maintenance")
    @Operation(summary = "Send drone to maintenance", description = "Updates drone status and creates a pending maintenance record.")
    public ResponseEntity<String> sendDroneToMaintenance(
            @PathVariable Long id,
            @RequestParam String description,
            @RequestParam Long technicianId) {
        
        // 1. Actualizar estado del dron
        Drone drone = droneService.getDroneById(id);
        drone.setStatus("MAINTENANCE");
        droneService.updateDrone(drone);

        // 2. Crear registro de mantenimiento
        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new IllegalArgumentException("Technician not found with id: " + technicianId));

        Maintenance maintenance = new Maintenance();
        maintenance.setDrone(drone);
        maintenance.setType("PREVENTIVE");
        maintenance.setStatus("PENDING");
        maintenance.setDescription(description);
        maintenance.setTechnician(technician);
        maintenance.setScheduledDate(LocalDate.now().plusDays(3));

        maintenanceRepository.save(maintenance);

        return ResponseEntity.ok("Drone " + id + " sent to maintenance successfully.");
    }
}