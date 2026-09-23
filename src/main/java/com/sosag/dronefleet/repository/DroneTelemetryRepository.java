package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.DroneTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing DroneTelemetry entities.
 *
 * <p>Spring Data JPA provides the basic CRUD operations automatically.</p>
 */
public interface DroneTelemetryRepository extends JpaRepository<DroneTelemetry, Long> {
}