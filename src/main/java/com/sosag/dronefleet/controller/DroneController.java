package com.sosag.dronefleet.controller;

import com.sosag.dronefleet.dto.DroneRequestDTO;
import com.sosag.dronefleet.dto.DroneResponseDTO;
import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import com.sosag.dronefleet.repository.DroneModelRepository;
import com.sosag.dronefleet.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Drone resources.
 *
 * <p>Exposes endpoints to create, retrieve, and list drones within the fleet.</p>
 */
@RestController
@RequestMapping("/api/v1/drones")
@Tag(name = "Drones", description = "Operations related to drone management and fleet tracking")
public class DroneController {

    private final DroneService droneService;
    private final DroneModelRepository droneModelRepository;

    public DroneController(DroneService droneService, DroneModelRepository droneModelRepository) {
        this.droneService = droneService;
        this.droneModelRepository = droneModelRepository;
    }

    @GetMapping
    @Operation(summary = "Get all drones", description = "Retrieves a list of all drones registered in the fleet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of drones"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DroneResponseDTO>> getAllDrones() {
        List<Drone> drones = droneService.getAllDrones();
        List<DroneResponseDTO> response = drones.stream()
                .map(DroneResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get drone by ID", description = "Retrieves a specific drone by its technical identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drone found"),
            @ApiResponse(responseCode = "404", description = "Drone not found")
    })
    public ResponseEntity<DroneResponseDTO> getDroneById(@PathVariable Long id) {
        Drone drone = droneService.getDroneById(id);
        return ResponseEntity.ok(DroneResponseDTO.fromEntity(drone));
    }

    @PostMapping
    @Operation(summary = "Create a new drone", description = "Registers a new drone in the fleet. The serial number is auto-generated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Drone successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input or Drone Model not found")
    })
    public ResponseEntity<DroneResponseDTO> createDrone(@Valid @RequestBody DroneRequestDTO request) {
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
}