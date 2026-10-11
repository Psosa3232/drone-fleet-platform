package com.sosag.dronefleet.dto;

/**
 * DTO for authentication responses.
 *
 * @param token JWT token for authenticated requests
 * @param email User's email address
 * @param role User's role in the system
 * @param message Status message
 */
public record AuthResponse(
        String token,
        String email,
        String role,
        String message
) {}