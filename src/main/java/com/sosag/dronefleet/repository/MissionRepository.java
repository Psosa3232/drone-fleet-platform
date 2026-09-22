package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing Mission entities.
 *
 * <p>Spring Data JPA provides the basic CRUD operations automatically.</p>
 */
public interface MissionRepository extends JpaRepository<Mission, Long> {
}