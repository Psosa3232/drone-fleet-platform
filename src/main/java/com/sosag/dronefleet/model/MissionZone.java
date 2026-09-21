package com.sosag.dronefleet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an operational zone where a drone mission can take place.
 *
 * <p>This entity stores the geographical and operational restrictions
 * associated with a mission zone.</p>
 *
 * <p>This entity is mapped to the {@code mission_zone} table
 * in PostgreSQL.</p>
 */
@Entity
@Table(name = "mission_zone")
public class MissionZone {

    /**
     * Unique identifier of the mission zone.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_zone_id")
    private Long missionZoneId;

    /**
     * Name of the mission zone.
     */
    @Column(name = "name")
    private String name;

    /**
     * Description of the mission zone.
     */
    @Column(name = "description")
    private String description;

    /**
     * Latitude of the mission zone.
     */
    @Column(name = "latitude")
    private BigDecimal latitude;

    /**
     * Longitude of the mission zone.
     */
    @Column(name = "longitude")
    private BigDecimal longitude;

    /**
     * Maximum permitted altitude within the mission zone.
     */
    @Column(name = "max_altitude")
    private BigDecimal maxAltitude;

    /**
     * Indicates whether the mission zone is currently active.
     */
    @Column(name = "active")
    private boolean active;

    /**
     * Date and time when the mission zone was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Returns the unique identifier of the mission zone.
     *
     * @return the mission zone identifier
     */
    public Long getMissionZoneId() {
        return missionZoneId;
    }

    /**
     * Sets the unique identifier of the mission zone.
     *
     * @param missionZoneId the mission zone identifier
     */
    public void setMissionZoneId(Long missionZoneId) {
        this.missionZoneId = missionZoneId;
    }

    /**
     * Returns the name of the mission zone.
     *
     * @return the mission zone name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the mission zone.
     *
     * @param name the mission zone name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the mission zone.
     *
     * @return the mission zone description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the mission zone.
     *
     * @param description the mission zone description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the latitude of the mission zone.
     *
     * @return the latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the mission zone.
     *
     * @param latitude the latitude
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude of the mission zone.
     *
     * @return the longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the mission zone.
     *
     * @param longitude the longitude
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the maximum permitted altitude of the mission zone.
     *
     * @return the maximum altitude
     */
    public BigDecimal getMaxAltitude() {
        return maxAltitude;
    }

    /**
     * Sets the maximum permitted altitude of the mission zone.
     *
     * @param maxAltitude the maximum altitude
     */
    public void setMaxAltitude(BigDecimal maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    /**
     * Returns whether the mission zone is active.
     *
     * @return {@code true} if the zone is active, otherwise {@code false}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the mission zone.
     *
     * @param active {@code true} to activate the zone, otherwise {@code false}
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the creation date and time of the mission zone.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date and time of the mission zone.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}