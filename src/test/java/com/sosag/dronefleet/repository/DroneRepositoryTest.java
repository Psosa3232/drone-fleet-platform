package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the DroneRepository.
 *
 * <p>This test verifies that a Drone can be persisted and retrieved
 * together with its relationship to a DroneModel.</p>
 */
@SpringBootTest
@Transactional
class DroneRepositoryTest {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    @Test
    void shouldSaveAndFindDrone() {

        DroneModel droneModel = new DroneModel();

        droneModel.setManufacturer("DJI");
        droneModel.setModelName("Mavic 3");
        droneModel.setBatteryCapacity(5000);
        droneModel.setMaxSpeed(new BigDecimal("75.00"));
        droneModel.setMaxFlightTime(46);
        droneModel.setMaxPayload(new BigDecimal("0.90"));

        DroneModel savedDroneModel = droneModelRepository.save(droneModel);

        Drone drone = new Drone();

        drone.setSerialNumber("A001");
        drone.setDroneModel(savedDroneModel);
        drone.setStatus("AVAILABLE");
        drone.setBatteryLevel(new BigDecimal("100.00"));
        drone.setTotalFlightHours(new BigDecimal("0.00"));
        drone.setPurchaseDate(LocalDate.now());

        Drone savedDrone = droneRepository.save(drone);

        assertNotNull(savedDrone.getDroneId());

        Drone foundDrone = droneRepository.findById(savedDrone.getDroneId())
                .orElseThrow();

        assertEquals("A001", foundDrone.getSerialNumber());
        assertEquals("AVAILABLE", foundDrone.getStatus());
        assertEquals("DJI", foundDrone.getDroneModel().getManufacturer());
        assertEquals("Mavic 3", foundDrone.getDroneModel().getModelName());
    }
}