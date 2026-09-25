package com.sosag.dronefleet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosag.dronefleet.dto.DroneRequestDTO;
import com.sosag.dronefleet.exception.DroneNotFoundException;
import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import com.sosag.dronefleet.repository.DroneModelRepository;
import com.sosag.dronefleet.service.DroneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link DroneController}.
 */
@WebMvcTest(DroneController.class)
class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DroneService droneService;

    @MockitoBean
    private DroneModelRepository droneModelRepository;

    @Test
    void shouldGetAllDrones() throws Exception {
        // Given
        DroneModel model = new DroneModel();
        model.setDroneModelId(1L);
        model.setManufacturer("DJI");
        model.setModelName("Mavic 3");

        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setSerialNumber("A001");
        drone.setDroneModel(model);
        drone.setStatus("AVAILABLE");
        drone.setBatteryLevel(new BigDecimal("100.00"));
        drone.setTotalFlightHours(BigDecimal.ZERO);
        drone.setCreatedAt(LocalDateTime.now());
        drone.setUpdatedAt(LocalDateTime.now());

        given(droneService.getAllDrones()).willReturn(List.of(drone));

        // When & Then
        mockMvc.perform(get("/api/v1/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber").value("A001"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }

    @Test
    void shouldGetDroneById() throws Exception {
        // Given
        DroneModel model = new DroneModel();
        model.setDroneModelId(1L);

        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setSerialNumber("A001");
        drone.setDroneModel(model);
        drone.setStatus("AVAILABLE");
        drone.setBatteryLevel(new BigDecimal("100.00"));
        drone.setTotalFlightHours(BigDecimal.ZERO);
        drone.setCreatedAt(LocalDateTime.now());
        drone.setUpdatedAt(LocalDateTime.now());

        given(droneService.getDroneById(1L)).willReturn(drone);

        // When & Then
        mockMvc.perform(get("/api/v1/drones/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("A001"));
    }

    @Test
    void shouldReturnNotFoundWhenDroneDoesNotExist() throws Exception {
        // Given
        given(droneService.getDroneById(999L))
                .willThrow(new DroneNotFoundException("Drone not found with id: 999"));

        // When & Then
        mockMvc.perform(get("/api/v1/drones/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Drone Not Found"));
    }

    @Test
    void shouldCreateDrone() throws Exception {
        // Given
        DroneModel model = new DroneModel();
        model.setDroneModelId(1L);
        model.setManufacturer("DJI");
        model.setModelName("Mavic 3");

        DroneRequestDTO request = new DroneRequestDTO(
                1L,
                "AVAILABLE",
                new BigDecimal("100.00"),
                BigDecimal.ZERO
        );

        Drone savedDrone = new Drone();
        savedDrone.setDroneId(1L);
        savedDrone.setSerialNumber("A001");
        savedDrone.setDroneModel(model);
        savedDrone.setStatus("AVAILABLE");
        savedDrone.setBatteryLevel(new BigDecimal("100.00"));
        savedDrone.setTotalFlightHours(BigDecimal.ZERO);
        savedDrone.setCreatedAt(LocalDateTime.now());
        savedDrone.setUpdatedAt(LocalDateTime.now());

        given(droneModelRepository.findById(1L)).willReturn(Optional.of(model));
        given(droneService.createDrone(any(Drone.class))).willReturn(savedDrone);

        // When & Then
        mockMvc.perform(post("/api/v1/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("A001"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void shouldReturnBadRequestWhenDroneModelNotFound() throws Exception {
        // Given
        DroneRequestDTO request = new DroneRequestDTO(
                999L,
                "AVAILABLE",
                new BigDecimal("100.00"),
                BigDecimal.ZERO
        );

        given(droneModelRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/v1/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        // Given - Request with null status (should fail validation)
        String invalidRequest = """
                {
                    "droneModelId": 1,
                    "status": null,
                    "batteryLevel": 100.00,
                    "totalFlightHours": 0.00
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/v1/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"));
    }
}