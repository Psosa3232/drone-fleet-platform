CREATE TABLE users (
                       user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       surname VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE TABLE drone_models (
                              drone_model_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              manufacturer VARCHAR(100) NOT NULL,
                              model_name VARCHAR(100) NOT NULL,
                              battery_capacity INT NOT NULL,
                              max_speed DECIMAL(5,2) NOT NULL,
                              max_flight_time INT NOT NULL,
                              max_payload DECIMAL(6,2) NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE TABLE mission_zone (
                              mission_zone_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              name VARCHAR(45) NOT NULL,
                              description VARCHAR(200) NOT NULL,
                              latitude DECIMAL(9,6) NOT NULL,
                              longitude DECIMAL(9,6) NOT NULL,
                              max_altitude DECIMAL(5,2) NOT NULL,
                              active BOOLEAN NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE TABLE drones (
                        drone_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        serial_number VARCHAR(4) NOT NULL UNIQUE,
                        drone_model_id BIGINT NOT NULL,
                        status VARCHAR(30) NOT NULL,
                        battery_level DECIMAL(5,2) NOT NULL,
                        total_flight_hours DECIMAL(10,2) NOT NULL DEFAULT 0,
                        purchase_date DATE,
                        created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                        CONSTRAINT fk_drone_model
                            FOREIGN KEY (drone_model_id)
                                REFERENCES drone_models(drone_model_id)
);


CREATE TABLE missions (
                          mission_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(500),
                          drone_id BIGINT NOT NULL,
                          operator_id BIGINT NOT NULL,
                          mission_zone_id BIGINT NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          priority VARCHAR(20) NOT NULL,
                          scheduled_start TIMESTAMP NOT NULL,
                          actual_start TIMESTAMP,
                          actual_end TIMESTAMP,
                          distance_km DECIMAL(10,2),
                          result VARCHAR(50),
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_mission_drone
                              FOREIGN KEY (drone_id)
                                  REFERENCES drones(drone_id),

                          CONSTRAINT fk_mission_operator
                              FOREIGN KEY (operator_id)
                                  REFERENCES users(user_id),

                          CONSTRAINT fk_mission_zone
                              FOREIGN KEY (mission_zone_id)
                                  REFERENCES mission_zone(mission_zone_id)
);


CREATE TABLE maintenance (
                             maintenance_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             drone_id BIGINT NOT NULL,
                             type VARCHAR(30) NOT NULL,
                             status VARCHAR(30) NOT NULL,
                             scheduled_date DATE NOT NULL,
                             performed_date DATE,
                             flight_hours DECIMAL(10,2),
                             description VARCHAR(500),
                             technician_id BIGINT NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT NOW(),

                             CONSTRAINT fk_maintenance_drone
                                 FOREIGN KEY (drone_id)
                                     REFERENCES drones(drone_id),

                             CONSTRAINT fk_maintenance_technician
                                 FOREIGN KEY (technician_id)
                                     REFERENCES users(user_id)
);


CREATE TABLE drone_telemetry (
                                 telemetry_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 drone_id BIGINT NOT NULL,
                                 mission_id BIGINT NOT NULL,
                                 timestamp TIMESTAMP NOT NULL,
                                 latitude DECIMAL(9,6) NOT NULL,
                                 longitude DECIMAL(9,6) NOT NULL,
                                 altitude DECIMAL(6,2) NOT NULL,
                                 speed DECIMAL(6,2) NOT NULL,
                                 battery_level DECIMAL(5,2) NOT NULL,
                                 temperature DECIMAL(5,2),
                                 distance_from_start DECIMAL(10,2),

                                 CONSTRAINT fk_telemetry_drone
                                     FOREIGN KEY (drone_id)
                                         REFERENCES drones(drone_id),

                                 CONSTRAINT fk_telemetry_mission
                                     FOREIGN KEY (mission_id)
                                         REFERENCES missions(mission_id)
);


