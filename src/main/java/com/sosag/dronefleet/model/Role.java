package com.sosag.dronefleet.model;

/**
 * Defines the roles available to users within the drone fleet platform.
 */
public enum Role {

    /**
     * Full access to the platform.
     */
    ADMIN,

    /**
     * User responsible for operating drones and managing missions.
     */
    OPERATOR,

    /**
     * User responsible for drone maintenance operations.
     */
    MAINTENANCE,

    /**
     * User responsible for data analysis and reporting.
     */
    ANALYST
}

