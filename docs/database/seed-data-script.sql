-- ============================================================================
-- DRONE FLEET PLATFORM - SEED DATA SCRIPT
-- ============================================================================
-- This script populates the database with initial data for development and
-- testing purposes. It clears existing data and inserts fresh records with
-- predictable IDs to ensure consistency across environments.
--
-- Execution order: Users -> Drone Models -> Mission Zones -> Drones -> Missions
-- ============================================================================

-- 1. CLEAR EXISTING DATA
-- Truncate all tables in reverse dependency order and restart ID sequences
TRUNCATE TABLE drone_telemetry RESTART IDENTITY CASCADE;
TRUNCATE TABLE maintenance RESTART IDENTITY CASCADE;
TRUNCATE TABLE missions RESTART IDENTITY CASCADE;
TRUNCATE TABLE drones RESTART IDENTITY CASCADE;
TRUNCATE TABLE mission_zone RESTART IDENTITY CASCADE;
TRUNCATE TABLE drone_models RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- 2. INSERT USERS
-- Creates 3 users with different roles: ADMIN, OPERATOR, and TECHNICIAN
-- IDs will be 1, 2, and 3 respectively
INSERT INTO users (name, surname, email, password, role, active) VALUES
('Pablo', 'Sosa', 'admin@dronefleet.com', 'password123', 'ADMIN', TRUE),
('John', 'Doe', 'operator@dronefleet.com', 'password123', 'OPERATOR', TRUE),
('Jane', 'Smith', 'tech@dronefleet.com', 'password123', 'TECHNICIAN', TRUE);

-- 3. INSERT DRONE MODELS
-- Creates 3 drone models with different specifications
-- IDs will be 1, 2, and 3 respectively
INSERT INTO drone_models (manufacturer, model_name, battery_capacity, max_speed, max_flight_time, max_payload) VALUES
('DJI', 'Mavic 3', 5000, 65.00, 46, 1.50),
('DJI', 'Mini 4 Pro', 3500, 50.00, 34, 0.50),
('DJI', 'Air 3', 4200, 60.00, 40, 1.00);

-- 4. INSERT MISSION ZONES
-- Creates 3 mission zones around Madrid with different altitude limits
-- IDs will be 1, 2, and 3 respectively
INSERT INTO mission_zone (name, description, latitude, longitude, max_altitude, active) VALUES
('North Madrid Zone', 'Vaguada Park area', 40.4700, -3.7100, 120.00, TRUE),
('South Madrid Zone', 'Casa de Campo area', 40.4100, -3.7500, 100.00, TRUE),
('Industrial Zone', 'Cobo Calleja industrial park', 40.5000, -3.6800, 80.00, TRUE);

-- 5. INSERT DRONES
-- Creates 5 drones distributed across the 3 models
-- IDs will be 1, 2, 3, 4, and 5 respectively
INSERT INTO drones (serial_number, drone_model_id, status, battery_level, total_flight_hours) VALUES
('D001', 1, 'AVAILABLE', 100.00, 0.00),
('D002', 1, 'AVAILABLE', 100.00, 0.00),
('D003', 2, 'AVAILABLE', 100.00, 0.00),
('D004', 2, 'AVAILABLE', 100.00, 0.00),
('D005', 3, 'AVAILABLE', 100.00, 0.00);

-- 6. INSERT MISSIONS
-- Creates 10 completed missions distributed across drones and zones
-- All missions use operator_id=2 (John Doe, the OPERATOR)
-- IDs will be 1 through 10 respectively
INSERT INTO missions (name, description, drone_id, operator_id, mission_zone_id, status, priority, scheduled_start) VALUES
('Mission Alpha', 'Infrastructure inspection', 1, 2, 1, 'COMPLETED', 'HIGH', NOW() - INTERVAL '5 hours'),
('Mission Beta', 'Terrain mapping', 2, 2, 2, 'COMPLETED', 'MEDIUM', NOW() - INTERVAL '4 hours'),
('Mission Gamma', 'Surveillance patrol', 3, 2, 3, 'COMPLETED', 'LOW', NOW() - INTERVAL '3 hours'),
('Mission Delta', 'Package delivery', 4, 2, 1, 'COMPLETED', 'HIGH', NOW() - INTERVAL '2 hours'),
('Mission Epsilon', 'Infrastructure inspection', 5, 2, 2, 'COMPLETED', 'MEDIUM', NOW() - INTERVAL '1 hour'),
('Mission Zeta', 'Terrain mapping', 1, 2, 3, 'COMPLETED', 'LOW', NOW() - INTERVAL '12 hours'),
('Mission Eta', 'Surveillance patrol', 2, 2, 1, 'COMPLETED', 'HIGH', NOW() - INTERVAL '24 hours'),
('Mission Theta', 'Package delivery', 3, 2, 2, 'COMPLETED', 'MEDIUM', NOW() - INTERVAL '36 hours'),
('Mission Iota', 'Infrastructure inspection', 4, 2, 3, 'COMPLETED', 'LOW', NOW() - INTERVAL '48 hours'),
('Mission Kappa', 'Terrain mapping', 5, 2, 1, 'COMPLETED', 'HIGH', NOW() - INTERVAL '60 hours');

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================
-- Run these queries to verify the data was inserted correctly:
--
-- SELECT COUNT(*) FROM users;           -- Expected: 3
-- SELECT COUNT(*) FROM drone_models;    -- Expected: 3
-- SELECT COUNT(*) FROM mission_zone;    -- Expected: 3
-- SELECT COUNT(*) FROM drones;          -- Expected: 5
-- SELECT COUNT(*) FROM missions;        -- Expected: 10
-- ============================================================================