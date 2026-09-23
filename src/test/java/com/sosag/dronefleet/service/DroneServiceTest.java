package com.sosag.dronefleet.service;

import com.sosag.dronefleet.exception.DroneNotFoundException;
import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import com.sosag.dronefleet.repository.DroneRepository;
import com.sosag.dronefleet.repository.DroneModelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DroneServiceTest {

    @Autowired
    private DroneService droneService;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    @Test
    void shouldGetDroneById() {

        DroneModel droneModel = new DroneModel();
        droneModel.setManufacturer("DJI");
        droneModel.setModelName("Mavic 3");
        droneModel.setBatteryCapacity(5000);
        droneModel.setMaxSpeed(new BigDecimal("20.00"));
        droneModel.setMaxFlightTime(46);
        droneModel.setMaxPayload(new BigDecimal("0.90"));

        droneModel = droneModelRepository.save(droneModel);

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
        assertEquals(droneModel.getDroneModelId(), foundDrone.getDroneModel().getDroneModelId());
    }

    @Test
    void shouldThrowExceptionWhenDroneDoesNotExist() {

        Long nonExistingDroneId = 999999L;

        assertThrows(
                DroneNotFoundException.class,
                () -> droneService.getDroneById(nonExistingDroneId)
        );
    }
}