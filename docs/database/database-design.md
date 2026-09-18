# Database Design

## Objective

Design a relational database to store the operational information
of the drone fleet management platform.

## Entities

The initial database consists of the following entities:

- Users
- Drone Models
- Drones
- Mission Zone
- Missions
- Maintenance
- Drone Telemetry

## Main Relationships

- A drone model can be associated with multiple drones.
- A drone can participate in multiple missions.
- A user can act as a mission operator.
- A user can act as a maintenance technician.
- A mission zone can be associated with multiple missions.
- A mission can generate multiple telemetry records.
- A drone can have multiple maintenance records.
- A drone can generate multiple telemetry records.

## Telemetry

The `drone_telemetry` table stores historical data generated
during missions.

The stored data includes:

- Position
- Altitude
- Speed
- Battery level
- Temperature
- Distance travelled
- Timestamp

This data will later be used for Data Engineering,
data analysis, and Machine Learning.