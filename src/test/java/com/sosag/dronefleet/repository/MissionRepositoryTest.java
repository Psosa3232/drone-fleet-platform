package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the MissionRepository.
 *
 * <p>This test verifies that a Mission can be persisted and retrieved
 * together with its relationships to Drone, User and MissionZone.</p>
 */
@SpringBootTest
@Transactional
class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionZoneRepository missionZoneRepository;

    @Test
    void shouldSaveAndFindMission() {

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

        User operator = new User();

        operator.setName("John");
        operator.setSurname("Doe");
        operator.setEmail("john.doe@test.com");
        operator.setPassword("test-password");
        operator.setRole(Role.OPERATOR);
        operator.setActive(true);

        User savedOperator = userRepository.save(operator);

        MissionZone missionZone = new MissionZone();

        missionZone.setName("Zone A");
        missionZone.setDescription("Test mission zone");
        missionZone.setLatitude(new BigDecimal("40.416775"));
        missionZone.setLongitude(new BigDecimal("-3.703790"));
        missionZone.setMaxAltitude(new BigDecimal("120.00"));
        missionZone.setActive(true);

        MissionZone savedMissionZone = missionZoneRepository.save(missionZone);

        Mission mission = new Mission();

        mission.setName("Test Mission");
        mission.setDescription("Repository integration test");
        mission.setDrone(savedDrone);
        mission.setOperator(savedOperator);
        mission.setMissionZone(savedMissionZone);
        mission.setStatus("SCHEDULED");
        mission.setPriority("NORMAL");
        mission.setScheduledStart(LocalDateTime.now().plusHours(1));
        mission.setDistanceKm(new BigDecimal("10.50"));
        mission.setResult("PENDING");

        Mission savedMission = missionRepository.save(mission);

        assertNotNull(savedMission.getMissionId());

        Mission foundMission = missionRepository.findById(savedMission.getMissionId())
                .orElseThrow();

        assertEquals("Test Mission", foundMission.getName());
        assertEquals("SCHEDULED", foundMission.getStatus());
        assertEquals("A001", foundMission.getDrone().getSerialNumber());
        assertEquals(
                "john.doe@test.com",
                foundMission.getOperator().getEmail()
        );
        assertEquals(
                "Zone A",
                foundMission.getMissionZone().getName()
        );
    }
}