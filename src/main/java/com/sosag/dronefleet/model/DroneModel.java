package com.sosag.dronefleet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a drone model available in the fleet platform.
 *
 * <p>This entity stores the technical specifications shared by
 * physical drones of the same model.</p>
 *
 * <p>This entity is mapped to the {@code drone_models} table
 * in PostgreSQL.</p>
 */
@Entity
@Table(name = "drone_models")
public class DroneModel {

    /**
     * Unique identifier of the drone model.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drone_model_id")
    private Long droneModelId;

    /**
     * Manufacturer of the drone model.
     */
    @Column(name = "manufacturer")
    private String manufacturer;

    /**
     * Name of the drone model.
     */
    @Column(name = "model_name")
    private String modelName;

    /**
     * Battery capacity of the drone model.
     */
    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    /**
     * Maximum speed of the drone model.
     */
    @Column(name = "max_speed")
    private BigDecimal maxSpeed;

    /**
     * Maximum flight time of the drone model in minutes.
     */
    @Column(name = "max_flight_time")
    private Integer maxFlightTime;

    /**
     * Maximum payload capacity of the drone model.
     */
    @Column(name = "max_payload")
    private BigDecimal maxPayload;

    /**
     * Date and time when the drone model was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Automatically sets the creation timestamp before the entity
     * is persisted for the first time.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Returns the unique identifier of the drone model.
     *
     * @return the drone model identifier
     */
    public Long getDroneModelId() {
        return droneModelId;
    }

    /**
     * Sets the unique identifier of the drone model.
     *
     * @param droneModelId the drone model identifier
     */
    public void setDroneModelId(Long droneModelId) {
        this.droneModelId = droneModelId;
    }

    /**
     * Returns the manufacturer of the drone model.
     *
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the manufacturer of the drone model.
     *
     * @param manufacturer the manufacturer
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Returns the name of the drone model.
     *
     * @return the model name
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the name of the drone model.
     *
     * @param modelName the model name
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * Returns the battery capacity of the drone model.
     *
     * @return the battery capacity
     */
    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    /**
     * Sets the battery capacity of the drone model.
     *
     * @param batteryCapacity the battery capacity
     */
    public void setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * Returns the maximum speed of the drone model.
     *
     * @return the maximum speed
     */
    public BigDecimal getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets the maximum speed of the drone model.
     *
     * @param maxSpeed the maximum speed
     */
    public void setMaxSpeed(BigDecimal maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Returns the maximum flight time of the drone model.
     *
     * @return the maximum flight time in minutes
     */
    public Integer getMaxFlightTime() {
        return maxFlightTime;
    }

    /**
     * Sets the maximum flight time of the drone model.
     *
     * @param maxFlightTime the maximum flight time
     */
    public void setMaxFlightTime(Integer maxFlightTime) {
        this.maxFlightTime = maxFlightTime;
    }

    /**
     * Returns the maximum payload capacity of the drone model.
     *
     * @return the maximum payload capacity
     */
    public BigDecimal getMaxPayload() {
        return maxPayload;
    }

    /**
     * Sets the maximum payload capacity of the drone model.
     *
     * @param maxPayload the maximum payload capacity
     */
    public void setMaxPayload(BigDecimal maxPayload) {
        this.maxPayload = maxPayload;
    }

    /**
     * Returns the creation date and time of the drone model.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date and time of the drone model.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}