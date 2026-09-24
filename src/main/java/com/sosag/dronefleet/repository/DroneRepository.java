package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing Drone entities.
 *
 * <p>Spring Data JPA provides the basic CRUD operations automatically.</p>
 */
public interface DroneRepository extends JpaRepository<Drone, Long> {

    /**
     * Finds the drone with the highest database identifier.
     *
     * <p>This method is used by the service layer to determine
     * the next available drone serial number.</p>
     *
     * @return the drone with the highest identifier, or {@code null}
     *         if no drones exist
     */
    Drone findTopByOrderByDroneIdDesc();
}