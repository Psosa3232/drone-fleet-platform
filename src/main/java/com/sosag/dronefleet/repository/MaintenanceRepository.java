package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing Maintenance entities.
 *
 * <p>Spring Data JPA provides the basic CRUD operations automatically.</p>
 */
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}