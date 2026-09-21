package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.MissionZone;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing mission zone data.
 *
 * <p>Spring Data JPA provides the implementation automatically,
 * allowing CRUD operations to be performed without writing SQL queries.</p>
 */
public interface MissionZoneRepository extends JpaRepository<MissionZone, Long> {
}