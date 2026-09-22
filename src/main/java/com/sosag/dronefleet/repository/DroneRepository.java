package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing Drone entities.
 *
 * <p>Spring Data JPA provides the basic CRUD operations automatically.</p>
 */
public interface DroneRepository extends JpaRepository<Drone, Long> {
}