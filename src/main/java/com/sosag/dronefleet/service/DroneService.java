package com.sosag.dronefleet.service;

import com.sosag.dronefleet.exception.DroneNotFoundException;
import com.sosag.dronefleet.model.Drone;
import com.sosag.dronefleet.repository.DroneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for managing drone-related business operations.
 *
 * <p>This service acts as an intermediate layer between the REST controllers
 * and the persistence layer. It uses {@link DroneRepository} to access
 * drone data stored in PostgreSQL.</p>
 */
@Service
public class DroneService {

    private final DroneRepository droneRepository;

    /**
     * Creates a new DroneService with the required repository dependency.
     *
     * @param droneRepository repository used to access drone data
     */
    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    /**
     * Retrieves a drone by its identifier.
     *
     * <p>If the drone does not exist, a {@link DroneNotFoundException}
     * is thrown.</p>
     *
     * @param id unique identifier of the drone
     * @return the drone associated with the specified identifier
     * @throws DroneNotFoundException if no drone exists with the specified identifier
     */
    public Drone getDroneById(Long id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new DroneNotFoundException(
                        "Drone not found with id: " + id
                ));
    }

    /**
     * Retrieves all drones stored in the database.
     *
     * <p>If no drones exist, an empty list is returned.</p>
     *
     * @return a list containing all drones
     */
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    /**
     * Creates and persists a new drone.
     *
     * <p>The drone serial number is generated automatically by the service.
     * The generated format consists of a letter followed by three digits,
     * for example {@code A001}, {@code A002}, and {@code A003}.</p>
     *
     * @param drone drone to create
     * @return the persisted drone with its generated identifier and serial number
     */
    public Drone createDrone(Drone drone) {

        Drone lastDrone = droneRepository.findTopByOrderByDroneIdDesc();

        String serialNumber;

        if (lastDrone == null) {
            serialNumber = "A001";
        } else {
            serialNumber = generateNextSerialNumber(lastDrone.getSerialNumber());
        }

        drone.setSerialNumber(serialNumber);

        return droneRepository.save(drone);
    }

    /**
     * Generates the next serial number based on the serial number
     * of the last registered drone.
     *
     * <p>The numeric portion is incremented until {@code 999}.
     * When the numeric portion reaches {@code 999}, the letter
     * portion is incremented and the numeric portion starts again
     * at {@code 001}.</p>
     *
     * @param currentSerialNumber serial number of the last registered drone
     * @return the next available serial number
     */
    private String generateNextSerialNumber(String currentSerialNumber) {

        char letter = currentSerialNumber.charAt(0);

        int number = Integer.parseInt(
                currentSerialNumber.substring(1)
        );

        if (number < 999) {
            number++;
        } else {
            letter++;
            number = 1;
        }

        return String.format("%c%03d", letter, number);
    }
}