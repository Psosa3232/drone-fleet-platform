package com.sosag.dronefleet.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service for managing the real-time state of drones in Redis.
 * Provides methods to update and retrieve the current state of
 * each drone (position, battery, speed, etc.) with low latency.
 */
@Service
public class RealTimeDroneService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Constructs the RealTimeDroneService with the provided RedisTemplate.
     *
     * @param redisTemplate the Redis template for data operations
     */
    public RealTimeDroneService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Updates the real-time state of a drone in Redis.
     * The state is stored as a Redis Hash with a TTL of 1 hour,
     * ensuring stale data is automatically cleaned up if a drone
     * stops reporting.
     *
     * @param serialNumber the serial number of the drone (e.g., "D001")
     * @param state a map containing the drone's current state attributes
     */
    public void updateDroneState(String serialNumber, Map<String, String> state) {
        String key = "drone:" + serialNumber + ":state";
        redisTemplate.opsForHash().putAll(key, state);
        // Expire the key after 1 hour if the drone stops reporting
        redisTemplate.expire(key, 3600, TimeUnit.SECONDS);
    }

    /**
     * Retrieves the current real-time state of a specific drone from Redis.
     *
     * @param serialNumber the serial number of the drone (e.g., "D001")
     * @return a map containing the drone's current state attributes,
     *         or an empty map if the drone is not tracked
     */
    public Map<Object, Object> getDroneState(String serialNumber) {
        String key = "drone:" + serialNumber + ":state";
        return redisTemplate.opsForHash().entries(key);
    }
}