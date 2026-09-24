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
 *
 * <p>These tests verify the interaction between the service layer,
 * repository layer, and PostgreSQL database.</p>
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

    /**
     * Verifies that a drone can be retrieved by its identifier.
     */
    @Test
    void shouldGetDroneById() {

        DroneModel droneModel = createDroneModel();

        Drone drone = new Drone();
        drone.setSerialNumber("A001");
        drone.setDroneModel(droneModel);
        drone.setStatus("AVAILABLE");
        drone.setBatteryLevel(new BigDecimal("95.00"));
        drone.setTotalFlightHours(new BigDecimal("10.50"));

        drone = droneRepository.save(drone);

        Drone foundDrone = droneService.getDroneById(drone.getDroneId());

        assertNotNull(foundDrone);
        assertEquals(drone.getDroneId(), foundDrone.getDroneId());
        assertEquals("A001", foundDrone.getSerialNumber());
        assertEquals("AVAILABLE", foundDrone.getStatus());
        assertEquals(
                droneModel.getDroneModelId(),
                foundDrone.getDroneModel().getDroneModelId()
        );
    }

    /**
     * Verifies that a DroneNotFoundException is thrown when
     * the requested drone does not exist.
     */
    @Test
    void shouldThrowExceptionWhenDroneDoesNotExist() {

        Long nonExistingDroneId = 999999L;

        assertThrows(
                DroneNotFoundException.class,
                () -> droneService.getDroneById(nonExistingDroneId)
        );
    }

    /**
     * Verifies that all persisted drones can be retrieved.
     */
    @Test
    void shouldGetAllDrones() {

        DroneModel droneModel = createDroneModel();

        Drone firstDrone = new Drone();
        firstDrone.setSerialNumber("A001");
        firstDrone.setDroneModel(droneModel);
        firstDrone.setStatus("AVAILABLE");
        firstDrone.setBatteryLevel(new BigDecimal("95.00"));
        firstDrone.setTotalFlightHours(new BigDecimal("10.50"));

        Drone secondDrone = new Drone();
        secondDrone.setSerialNumber("A002");
        secondDrone.setDroneModel(droneModel);
        secondDrone.setStatus("AVAILABLE");
        secondDrone.setBatteryLevel(new BigDecimal("80.00"));
        secondDrone.setTotalFlightHours(new BigDecimal("25.00"));

        droneRepository.save(firstDrone);
        droneRepository.save(secondDrone);

        List<Drone> drones = droneService.getAllDrones();

        assertNotNull(drones);
        assertEquals(2, drones.size());

        assertTrue(
                drones.stream()
                        .anyMatch(drone -> "A001".equals(drone.getSerialNumber()))
        );

        assertTrue(
                drones.stream()
                        .anyMatch(drone -> "A002".equals(drone.getSerialNumber()))
        );
    }

    /**
     * Verifies that the service automatically generates the first
     * drone serial number when no drones exist.
     */
    @Test
    void shouldGenerateFirstDroneSerialNumber() {

        droneRepository.deleteAll();

        DroneModel droneModel = createDroneModel();

        Drone drone = new Drone();
        drone.setDroneModel(droneModel);
        drone.setStatus("AVAILABLE");
        drone.setBatteryLevel(new BigDecimal("100.00"));
        drone.setTotalFlightHours(BigDecimal.ZERO);

        Drone savedDrone = droneService.createDrone(drone);

        assertNotNull(savedDrone);
        assertEquals("A001", savedDrone.getSerialNumber());
    }

    /**
     * Verifies that the service generates the next serial number
     * based on the last registered drone.
     */
    @Test
    void shouldGenerateNextDroneSerialNumber() {

        droneRepository.deleteAll();

        DroneModel droneModel = createDroneModel();

        Drone firstDrone = new Drone();
        firstDrone.setDroneModel(droneModel);
        firstDrone.setStatus("AVAILABLE");
        firstDrone.setBatteryLevel(new BigDecimal("100.00"));
        firstDrone.setTotalFlightHours(BigDecimal.ZERO);

        Drone firstSavedDrone = droneService.createDrone(firstDrone);

        Drone secondDrone = new Drone();
        secondDrone.setDroneModel(droneModel);
        secondDrone.setStatus("AVAILABLE");
        secondDrone.setBatteryLevel(new BigDecimal("100.00"));
        secondDrone.setTotalFlightHours(BigDecimal.ZERO);

        Drone secondSavedDrone = droneService.createDrone(secondDrone);

        assertEquals("A001", firstSavedDrone.getSerialNumber());
        assertEquals("A002", secondSavedDrone.getSerialNumber());
    }

    /**
     * Creates and persists a drone model used by the integration tests.
     *
     * @return persisted drone model
     */
    private DroneModel createDroneModel() {

        DroneModel droneModel = new DroneModel();
        droneModel.setManufacturer("DJI");
        droneModel.setModelName("Mavic 3");
        droneModel.setBatteryCapacity(5000);
        droneModel.setMaxSpeed(new BigDecimal("20.00"));
        droneModel.setMaxFlightTime(46);
        droneModel.setMaxPayload(new BigDecimal("0.90"));

        return droneModelRepository.save(droneModel);
    }
}