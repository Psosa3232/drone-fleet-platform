package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing and managing Maintenance entities.
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    /**
     * Finds all maintenance records with pending or scheduled status.
     *
     * @return list of maintenance records requiring attention
     */
    @Query("SELECT m FROM Maintenance m WHERE m.status = 'PENDING' OR m.status = 'SCHEDULED'")
    List<Maintenance> findPendingOrScheduledMaintenances();
}