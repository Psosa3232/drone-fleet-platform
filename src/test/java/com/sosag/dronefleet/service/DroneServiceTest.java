package com.sosag.dronefleet.service;

import com.sosag.dronefleet.exception.DroneNotFoundException;
import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import com.sosag.dronefleet.repository.DroneModelRepository;
import com.sosag.dronefleet.repository.DroneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link DroneService}.
 */
@SpringBootTest
@Transactional
class DroneServiceTest {

    @Autowired
    private DroneService droneService;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    private DroneModel createValidDroneModel() {
        DroneModel model = new DroneModel();
        model.setManufacturer("DJI");
        model.setModelName("Mavic 3");
        model.setBatteryCapacity(5000);
        model.setMaxSpeed(new BigDecimal("65.0"));
        model.setMaxFlightTime(30);
        model.setMaxPayload(new BigDecimal("1.5"));
        return model;
    }

    @Test
    void shouldGetAllDrones() {
        // Given
        DroneModel savedModel = droneModelRepository.save(createValidDroneModel());

        Drone drone = new Drone();
        drone.setSerialNumber("D001"); // Máximo 4 caracteres
        drone.setDroneModel(savedModel);
        drone.setStatus("AVAILABLE");
        drone.setBatteryLevel(new BigDecimal("100.00"));
        drone.setTotalFlightHours(BigDecimal.ZERO);
        droneRepository.save(drone);

        // When
        List<Drone> drones = droneService.getAllDrones();

        // Then
        assertNotNull(drones);
        assertFalse(drones.isEmpty());
        assertTrue(drones.stream().anyMatch(d -> d.getSerialNumber().equals("D001")));
    }

    @Test
    void shouldGetDroneById() {
        // Given
        DroneModel savedModel = droneModelRepository.save(createValidDroneModel());

        Drone drone = new Drone();
        drone.setSerialNumber("D002"); // Máximo 4 caracteres
        drone.setDroneModel(savedModel);
        drone.setStatus("AVAILABLE");
        drone.setBatteryLevel(new BigDecimal("100.00"));
        drone.setTotalFlightHours(BigDecimal.ZERO);
        Drone savedDrone = droneRepository.save(drone);

        // When
        Drone foundDrone = droneService.getDroneById(savedDrone.getDroneId());

        // Then
        assertNotNull(foundDrone);
        assertEquals("D002", foundDrone.getSerialNumber());
        assertEquals("AVAILABLE", foundDrone.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDroneNotFound() {
        assertThrows(DroneNotFoundException.class, () -> {
            droneService.getDroneById(999L);
        });
    }

    @Test
    void shouldCreateDrone() {
        // Given
        DroneModel savedModel = droneModelRepository.save(createValidDroneModel());

        Drone droneToCreate = new Drone();
        // El servicio puede generar su propio serial (ej. "A001"), así que verificamos que no sea nulo
        droneToCreate.setSerialNumber("D003");
        droneToCreate.setDroneModel(savedModel);
        droneToCreate.setStatus("AVAILABLE");
        droneToCreate.setBatteryLevel(new BigDecimal("100.00"));
        droneToCreate.setTotalFlightHours(BigDecimal.ZERO);

        // When
        Drone createdDrone = droneService.createDrone(droneToCreate);

        // Then
        assertNotNull(createdDrone);
        assertNotNull(createdDrone.getDroneId());
        assertNotNull(createdDrone.getSerialNumber()); // Verifica que el servicio asignó uno (ej. "A001")
        assertEquals("AVAILABLE", createdDrone.getStatus());
    }
}