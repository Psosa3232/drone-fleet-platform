package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.model.DroneModel;
import com.sosag.dronefleet.model.DroneTelemetry;
import com.sosag.dronefleet.model.Mission;
import com.sosag.dronefleet.model.MissionZone;
import com.sosag.dronefleet.model.Role;
import com.sosag.dronefleet.model.User;
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
 * Integration tests for the DroneTelemetryRepository.
 *
 * <p>This test verifies that a telemetry record can be persisted
 * and retrieved together with its relationships to Drone and Mission.</p>
 */
@SpringBootTest
@Transactional
class DroneTelemetryRepositoryTest {

    @Autowired
    private DroneTelemetryRepository droneTelemetryRepository;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionZoneRepository missionZoneRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Test
    void shouldSaveAndFindDroneTelemetry() {

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
        drone.setStatus("IN_FLIGHT");
        drone.setBatteryLevel(new BigDecimal("85.00"));
        drone.setTotalFlightHours(new BigDecimal("25.50"));
        drone.setPurchaseDate(LocalDate.now());

        Drone savedDrone = droneRepository.save(drone);

        User operator = new User();

        operator.setName("John");
        operator.setSurname("Doe");
        operator.setEmail("john.telemetry@test.com");
        operator.setPassword("test-password");
        operator.setRole(Role.OPERATOR);
        operator.setActive(true);

        User savedOperator = userRepository.save(operator);

        MissionZone missionZone = new MissionZone();

        missionZone.setName("Zone A");
        missionZone.setDescription("Telemetry test zone");
        missionZone.setLatitude(new BigDecimal("40.416775"));
        missionZone.setLongitude(new BigDecimal("-3.703790"));
        missionZone.setMaxAltitude(new BigDecimal("120.00"));
        missionZone.setActive(true);

        MissionZone savedMissionZone = missionZoneRepository.save(missionZone);

        Mission mission = new Mission();

        mission.setName("Telemetry Test Mission");
        mission.setDescription("Mission used for telemetry integration testing");
        mission.setDrone(savedDrone);
        mission.setOperator(savedOperator);
        mission.setMissionZone(savedMissionZone);
        mission.setStatus("IN_PROGRESS");
        mission.setPriority("NORMAL");
        mission.setScheduledStart(LocalDateTime.now().minusMinutes(30));
        mission.setActualStart(LocalDateTime.now().minusMinutes(20));
        mission.setDistanceKm(new BigDecimal("10.50"));
        mission.setResult("IN_PROGRESS");

        Mission savedMission = missionRepository.save(mission);

        DroneTelemetry telemetry = new DroneTelemetry();

        telemetry.setDrone(savedDrone);
        telemetry.setMission(savedMission);
        telemetry.setTimestamp(LocalDateTime.now());
        telemetry.setLatitude(new BigDecimal("40.416775"));
        telemetry.setLongitude(new BigDecimal("-3.703790"));
        telemetry.setAltitude(new BigDecimal("85.50"));
        telemetry.setSpeed(new BigDecimal("42.30"));
        telemetry.setBatteryLevel(new BigDecimal("82.50"));
        telemetry.setTemperature(new BigDecimal("38.20"));
        telemetry.setDistanceFromStart(new BigDecimal("4.75"));

        DroneTelemetry savedTelemetry = droneTelemetryRepository.save(telemetry);

        assertNotNull(savedTelemetry.getTelemetryId());

        DroneTelemetry foundTelemetry = droneTelemetryRepository
                .findById(savedTelemetry.getTelemetryId())
                .orElseThrow();

        assertEquals(
                "A001",
                foundTelemetry.getDrone().getSerialNumber()
        );

        assertEquals(
                "Telemetry Test Mission",
                foundTelemetry.getMission().getName()
        );

        assertEquals(
                new BigDecimal("40.416775"),
                foundTelemetry.getLatitude()
        );

        assertEquals(
                new BigDecimal("85.50"),
                foundTelemetry.getAltitude()
        );

        assertEquals(
                new BigDecimal("82.50"),
                foundTelemetry.getBatteryLevel()
        );

        assertEquals(
                new BigDecimal("38.20"),
                foundTelemetry.getTemperature()
        );
    }
}