package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.DroneModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for the {@link DroneModelRepository}.
 *
 * <p>This test verifies that drone model data can be persisted
 * and retrieved correctly using Spring Data JPA and PostgreSQL.</p>
 */
@SpringBootTest
@Transactional
class DroneModelRepositoryTest {

    /**
     * Repository used to access drone model data.
     */
    @Autowired
    private DroneModelRepository droneModelRepository;

    /**
     * Verifies that a drone model can be saved to the database
     * and subsequently retrieved by its generated identifier.
     */
    @Test
    void shouldSaveAndFindDroneModel() {

        DroneModel droneModel = new DroneModel();

        droneModel.setManufacturer("DJI");
        droneModel.setModelName("Mavic 3");
        droneModel.setBatteryCapacity(5000);
        droneModel.setMaxSpeed(new BigDecimal("75.00"));
        droneModel.setMaxFlightTime(46);
        droneModel.setMaxPayload(new BigDecimal("0.90"));
        droneModel.setCreatedAt(LocalDateTime.now());

        DroneModel savedDroneModel = droneModelRepository.save(droneModel);

        assertNotNull(savedDroneModel.getDroneModelId());

        var foundDroneModel =
                droneModelRepository.findById(savedDroneModel.getDroneModelId());

        assertTrue(foundDroneModel.isPresent());
        assertEquals("DJI", foundDroneModel.get().getManufacturer());
        assertEquals("Mavic 3", foundDroneModel.get().getModelName());
    }
}