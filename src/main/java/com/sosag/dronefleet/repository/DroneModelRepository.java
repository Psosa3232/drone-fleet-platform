package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.DroneModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing drone model data.
 *
 * <p>Spring Data JPA provides the implementation automatically,
 * allowing CRUD operations to be performed without writing SQL queries.</p>
 */
public interface DroneModelRepository extends JpaRepository<DroneModel, Long> {
}