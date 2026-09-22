package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import com.sosag.dronefleet.model.Maintenance;
import com.sosag.dronefleet.model.Role;
import com.sosag.dronefleet.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the MaintenanceRepository.
 *
 * <p>This test verifies that a Maintenance entity can be persisted
 * and retrieved together with its relationships to Drone and User.</p>
 */
@SpringBootTest
@Transactional
class MaintenanceRepositoryTest {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindMaintenance() {

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
        drone.setTotalFlightHours(new BigDecimal("25.50"));
        drone.setPurchaseDate(LocalDate.now());

        Drone savedDrone = droneRepository.save(drone);

        User technician = new User();

        technician.setName("Jane");
        technician.setSurname("Smith");
        technician.setEmail("jane.smith@test.com");
        technician.setPassword("test-password");
        technician.setRole(Role.MAINTENANCE);
        technician.setActive(true);

        User savedTechnician = userRepository.save(technician);

        Maintenance maintenance = new Maintenance();

        maintenance.setDrone(savedDrone);
        maintenance.setType("PREVENTIVE");
        maintenance.setStatus("SCHEDULED");
        maintenance.setScheduledDate(LocalDate.now().plusDays(7));
        maintenance.setFlightHours(new BigDecimal("25.50"));
        maintenance.setDescription("Scheduled preventive maintenance");
        maintenance.setTechnician(savedTechnician);

        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);

        assertNotNull(savedMaintenance.getMaintenanceId());

        Maintenance foundMaintenance = maintenanceRepository
                .findById(savedMaintenance.getMaintenanceId())
                .orElseThrow();

        assertEquals("PREVENTIVE", foundMaintenance.getType());
        assertEquals("SCHEDULED", foundMaintenance.getStatus());
        assertEquals(
                "A001",
                foundMaintenance.getDrone().getSerialNumber()
        );
        assertEquals(
                "jane.smith@test.com",
                foundMaintenance.getTechnician().getEmail()
        );
    }
}