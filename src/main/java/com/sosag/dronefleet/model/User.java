package com.sosag.dronefleet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Represents a user registered in the drone fleet management platform.
 *
 * <p>A user can have different roles within the system, such as operator
 * or maintenance technician.</p>
 *
 * <p>This entity is mapped to the {@code users} table in PostgreSQL.</p>
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * First name of the user.
     */
    @Column(name = "name")
    private String name;

    /**
     * Last name of the user.
     */
    @Column(name = "surname")
    private String surname;

    /**
     * Email address used to identify the user.
     */
    @Column(name = "email")
    private String email;

    /**
     * Password associated with the user account.
     *
     * <p>The password must be stored securely using password hashing
     * before being persisted.</p>
     */
    @Column(name = "password")
    private String password;

    /**
     * Role assigned to the user within the platform.
     * Role assigned to the user and stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    /**
     * Indicates whether the user account is currently active.
     */
    @Column(name = "active")
    private boolean active;

    /**
     * Date and time when the user was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Date and time when the user was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user identifier
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param userId the user identifier
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the user's first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the first name of the user.
     *
     * @param name the user's first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the user's last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the last name of the user.
     *
     * @param surname the user's last name
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the user's email address.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the role assigned to the user.
     *
     * @return the user's role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role assigned to the user.
     *
     * @param role the user's role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns whether the user account is active.
     *
     * @return {@code true} if the account is active, otherwise {@code false}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the user account.
     *
     * @param active {@code true} to activate the account,
     *               {@code false} to deactivate it
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the creation date and time of the user.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date and time of the user.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the last update date and time of the user.
     *
     * @return the last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update date and time of the user.
     *
     * @param updatedAt the last update timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}