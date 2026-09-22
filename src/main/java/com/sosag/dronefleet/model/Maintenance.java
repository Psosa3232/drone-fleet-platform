package com.sosag.dronefleet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a maintenance operation performed on a drone.
 *
 * <p>A maintenance record is associated with a drone and a
 * maintenance technician.</p>
 *
 * <p>This entity is mapped to the {@code maintenance} table
 * in PostgreSQL.</p>
 */
@Entity
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Long maintenanceId;

    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "performed_date")
    private LocalDate performedDate;

    @Column(name = "flight_hours")
    private BigDecimal flightHours;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = false)
    private User technician;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Initializes the creation timestamp before
     * the entity is persisted for the first time.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDate getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(LocalDate performedDate) {
        this.performedDate = performedDate;
    }

    public BigDecimal getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(BigDecimal flightHours) {
        this.flightHours = flightHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}