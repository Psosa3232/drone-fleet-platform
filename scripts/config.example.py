"""
config.example.py - Configuration template for drone telemetry generation.

This module defines the minimum and maximum values for each telemetry metric,
simulating realistic data ranges based on Kaggle datasets for DJI/PX4 drones.

IMPORTANT: Copy this file to config.py and fill in your actual credentials.
NEVER commit config.py with real credentials to version control.
"""

# ============================================================
# DATA RANGES (based on Kaggle drone telemetry datasets)
# ============================================================
# These ranges are based on real datasets from DJI/PX4 drones.
# You can modify them to simulate different scenarios.

RANGES = {
    "altitude": {
        "min": 0.0,        # meters (ground level)
        "max": 120.0,      # meters (EU legal limit)
        "unit": "m",
        "description": "Altitude above ground level"
    },
    "speed": {
        "min": 0.0,        # km/h (stationary)
        "max": 65.0,       # km/h (DJI Mavic 3 max speed)
        "unit": "km/h",
        "description": "Horizontal speed"
    },
    "battery_level": {
        "min": 0.0,        # percent (depleted)
        "max": 100.0,      # percent (fully charged)
        "unit": "%",
        "description": "Battery level"
    },
    "temperature": {
        "min": 15.0,       # Celsius (night/winter)
        "max": 55.0,       # Celsius (motor during flight)
        "unit": "C",
        "description": "Motor/ambient temperature"
    },
    "latitude": {
        "min": 40.35,      # South of Madrid
        "max": 40.50,      # North of Madrid
        "unit": "degrees",
        "description": "GPS latitude"
    },
    "longitude": {
        "min": -3.80,      # West of Madrid
        "max": -3.60,      # East of Madrid
        "unit": "degrees",
        "description": "GPS longitude"
    },
    "distance_from_start": {
        "min": 0.0,        # meters (takeoff point)
        "max": 5000.0,     # meters (5 km radius)
        "unit": "m",
        "description": "Distance from takeoff point"
    }
}

# ============================================================
# SIMULATION PARAMETERS
# ============================================================
NUM_DRONES = 5              # Number of simulated drones
NUM_MISSIONS = 10           # Number of missions
RECORDS_PER_MISSION = 200   # Records per mission (every 30s = ~1.5h of flight)
INTERVAL_SECONDS = 30       # Interval between records

# ============================================================
# DATABASE CONNECTION (PostgreSQL)
# ============================================================
# When running locally, use "localhost" as host.
# When running inside Docker Compose, use "db" as host.
DB_CONFIG = {
    "host": "YOUR_DB_HOST",          # e.g., "localhost" or "db" (Docker)
    "port": 5432,
    "database": "YOUR_DB_NAME",      # e.g., "drone_fleet"
    "user": "YOUR_DB_USER",          # e.g., "postgres"
    "password": "YOUR_DB_PASSWORD"   # Your PostgreSQL password
}

# ============================================================
# CACHE CONNECTION (Redis)
# ============================================================
# When running locally, use "localhost" as host.
# When running inside Docker Compose, use "redis" as host.
REDIS_CONFIG = {
    "host": "YOUR_REDIS_HOST",       # e.g., "localhost" or "redis" (Docker)
    "port": 6379,
    "db": 0
}

# ============================================================
# OUTPUT PATH
# ============================================================
OUTPUT_DIR = "output"