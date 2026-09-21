package com.sosag.dronefleet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a physical drone operated within the drone fleet platform.
 *
 * <p>This entity stores the operational information of an individual drone,
 * including its serial number, current status, battery level and accumulated
 * flight hours.</p>
 *
 * <p>This entity is mapped to the {@code drones} table in PostgreSQL.</p>
 */
@Entity
@Table(name = "drones")
public class Drone {

    /**
     * Unique identifier of the drone.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drone_id")
    private Long droneId;

    /**
     * Unique serial number assigned to the drone.
     */
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * Identifier of the drone model associated with this drone.
     */
    @Column(name = "drone_model_id")
    private Long droneModelId;

    /**
     * Current operational status of the drone.
     */
    @Column(name = "status")
    private String status;

    /**
     * Current battery level of the drone.
     */
    @Column(name = "battery_level")
    private BigDecimal batteryLevel;

    /**
     * Total accumulated flight hours of the drone.
     */
    @Column(name = "total_flight_hours")
    private BigDecimal totalFlightHours;

    /**
     * Date when the drone was purchased.
     */
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    /**
     * Date and time when the drone was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Date and time when the drone was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Returns the unique identifier of the drone.
     *
     * @return the drone identifier
     */
    public Long getDroneId() {
        return droneId;
    }

    /**
     * Sets the unique identifier of the drone.
     *
     * @param droneId the drone identifier
     */
    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    /**
     * Returns the serial number of the drone.
     *
     * @return the serial number
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the serial number of the drone.
     *
     * @param serialNumber the serial number
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Returns the identifier of the associated drone model.
     *
     * @return the drone model identifier
     */
    public Long getDroneModelId() {
        return droneModelId;
    }

    /**
     * Sets the identifier of the associated drone model.
     *
     * @param droneModelId the drone model identifier
     */
    public void setDroneModelId(Long droneModelId) {
        this.droneModelId = droneModelId;
    }

    /**
     * Returns the current operational status of the drone.
     *
     * @return the drone status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current operational status of the drone.
     *
     * @param status the drone status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the current battery level of the drone.
     *
     * @return the battery level
     */
    public BigDecimal getBatteryLevel() {
        return batteryLevel;
    }

    /**
     * Sets the current battery level of the drone.
     *
     * @param batteryLevel the battery level
     */
    public void setBatteryLevel(BigDecimal batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    /**
     * Returns the total accumulated flight hours of the drone.
     *
     * @return the total flight hours
     */
    public BigDecimal getTotalFlightHours() {
        return totalFlightHours;
    }

    /**
     * Sets the total accumulated flight hours of the drone.
     *
     * @param totalFlightHours the total flight hours
     */
    public void setTotalFlightHours(BigDecimal totalFlightHours) {
        this.totalFlightHours = totalFlightHours;
    }

    /**
     * Returns the purchase date of the drone.
     *
     * @return the purchase date
     */
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Sets the purchase date of the drone.
     *
     * @param purchaseDate the purchase date
     */
    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Returns the creation date and time of the drone.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date and time of the drone.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the date and time when the drone was last updated.
     *
     * @return the last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the date and time when the drone was last updated.
     *
     * @param updatedAt the last update timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}