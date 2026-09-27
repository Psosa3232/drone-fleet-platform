"""
generate_data.py - Drone telemetry data generator.

Generates realistic telemetry data based on the ranges configured in config.py
and inserts it into PostgreSQL, respecting the DroneTelemetry entity structure.
"""

import pandas as pd
import numpy as np
import psycopg2
from psycopg2.extras import execute_values
from datetime import datetime, timedelta
import os
import sys

# Import configuration
from config import RANGES, NUM_DRONES, NUM_MISSIONS, RECORDS_PER_MISSION, INTERVAL_SECONDS, DB_CONFIG, OUTPUT_DIR


def generate_telemetry_data():
    """
    Generates realistic telemetry data within configured ranges.
    Simulates real drone behavior (takeoff, cruise, return, landing).
    """
    print("Generating telemetry data...")
    print(f"   {NUM_DRONES} drones x {NUM_MISSIONS} missions x {RECORDS_PER_MISSION} records")
    print(f"   Total expected: {NUM_DRONES * NUM_MISSIONS * RECORDS_PER_MISSION} records")
    print()
    
    data = []
    
    for mission_idx in range(1, NUM_MISSIONS + 1):
        # Assign drone to mission (rotating)
        drone_id = ((mission_idx - 1) % NUM_DRONES) + 1
        
        # Random starting point within lat/lon range
        start_lat = np.random.uniform(RANGES["latitude"]["min"], RANGES["latitude"]["max"])
        start_lon = np.random.uniform(RANGES["longitude"]["min"], RANGES["longitude"]["max"])
        
        current_lat = start_lat
        current_lon = start_lon
        
        # Initial state
        battery = 100.0
        altitude = 0.0
        speed = 0.0
        temperature = np.random.uniform(RANGES["temperature"]["min"], RANGES["temperature"]["min"] + 5)
        distance_from_start = 0.0
        
        # Mission start time (missions on different days)
        mission_start = datetime.now() - timedelta(days=NUM_MISSIONS - mission_idx, hours=8)
        current_time = mission_start
        
        # Flight phases: 0=pre-flight, 1=takeoff, 2=cruise, 3=return, 4=landing
        flight_phase = 0
        
        for record_idx in range(RECORDS_PER_MISSION):
            current_time += timedelta(seconds=INTERVAL_SECONDS)
            
            # Phase transitions
            if record_idx == 0:
                flight_phase = 1  # Takeoff
            elif record_idx == 20:
                flight_phase = 2  # Cruise
            elif record_idx == int(RECORDS_PER_MISSION * 0.8):
                flight_phase = 3  # Return
            elif record_idx >= RECORDS_PER_MISSION - 10:
                flight_phase = 4  # Landing
            
            # === PHASE-BASED SIMULATION ===
            
            if flight_phase == 1:  # TAKEOFF
                altitude += np.random.uniform(2, 5)
                altitude = min(altitude, RANGES["altitude"]["max"] * 0.8)
                speed += np.random.uniform(1, 3)
                battery -= np.random.uniform(0.3, 0.6)
                temperature += np.random.uniform(1, 3)
                
            elif flight_phase == 2:  # CRUISE
                # Realistic movement (random walk)
                current_lat += np.random.uniform(-0.0008, 0.0008)
                current_lon += np.random.uniform(-0.0008, 0.0008)
                
                # Keep within ranges
                current_lat = np.clip(current_lat, RANGES["latitude"]["min"], RANGES["latitude"]["max"])
                current_lon = np.clip(current_lon, RANGES["longitude"]["min"], RANGES["longitude"]["max"])
                
                altitude = 80 + np.random.uniform(-15, 15)
                speed = 45 + np.random.uniform(-10, 10)
                battery -= np.random.uniform(0.15, 0.25)
                temperature = 45 + np.random.uniform(-5, 5)
                
            elif flight_phase == 3:  # RETURN
                # Return to starting point
                lat_diff = start_lat - current_lat
                lon_diff = start_lon - current_lon
                current_lat += lat_diff * 0.05
                current_lon += lon_diff * 0.05
                
                altitude = 70 + np.random.uniform(-10, 10)
                speed = 55 + np.random.uniform(-5, 5)
                battery -= np.random.uniform(0.2, 0.3)
                
            elif flight_phase == 4:  # LANDING
                altitude -= np.random.uniform(3, 8)
                altitude = max(0, altitude)
                speed -= np.random.uniform(2, 4)
                speed = max(0, speed)
                battery -= np.random.uniform(0.1, 0.2)
                temperature -= np.random.uniform(1, 2)
            
            # Calculate distance from start (simplified Haversine formula)
            lat_diff = current_lat - start_lat
            lon_diff = current_lon - start_lon
            distance_from_start = np.sqrt(lat_diff**2 + lon_diff**2) * 111000  # in meters
            
            # Add realistic noise (like in real Kaggle datasets)
            latitude_noisy = current_lat + np.random.normal(0, 0.00001)
            longitude_noisy = current_lon + np.random.normal(0, 0.00001)
            
            # Ensure battery does not go below 0
            battery = max(0, battery)
            
            # Ensure all values are within ranges
            altitude = max(RANGES["altitude"]["min"], min(RANGES["altitude"]["max"], altitude))
            speed = max(RANGES["speed"]["min"], min(RANGES["speed"]["max"], speed))
            temperature = max(RANGES["temperature"]["min"], min(RANGES["temperature"]["max"], temperature))
            
            data.append({
                "drone_id": drone_id,
                "mission_id": mission_idx,
                "timestamp": current_time,
                "latitude": round(latitude_noisy, 6),
                "longitude": round(longitude_noisy, 6),
                "altitude": round(altitude, 1),
                "speed": round(speed, 1),
                "battery_level": round(battery, 1),
                "temperature": round(temperature, 1),
                "distance_from_start": round(distance_from_start, 1)
            })
    
    df = pd.DataFrame(data)
    print(f"Data generated: {len(df)} records")
    return df


def calculate_statistics(df):
    """
    Calculates descriptive statistics and ranges for each metric.
    """
    print("\nDESCRIPTIVE STATISTICS")
    print("=" * 70)
    
    metrics = ["altitude", "speed", "battery_level", "temperature", "distance_from_start"]
    
    stats = {}
    for metric in metrics:
        col = df[metric]
        stats[metric] = {
            "min": col.min(),
            "max": col.max(),
            "mean": col.mean(),
            "median": col.median(),
            "std": col.std(),
            "p25": col.quantile(0.25),
            "p75": col.quantile(0.75),
            "range": col.max() - col.min()
        }
        
        print(f"\n{metric.upper().replace('_', ' ')}:")
        print(f"   Minimum:    {stats[metric]['min']:>10.2f}")
        print(f"   Maximum:    {stats[metric]['max']:>10.2f}")
        print(f"   Mean:       {stats[metric]['mean']:>10.2f}")
        print(f"   Median:     {stats[metric]['median']:>10.2f}")
        print(f"   Std Dev:    {stats[metric]['std']:>10.2f}")
        print(f"   P25:        {stats[metric]['p25']:>10.2f}")
        print(f"   P75:        {stats[metric]['p75']:>10.2f}")
        print(f"   Range:      {stats[metric]['range']:>10.2f}")
    
    return stats


def insert_into_postgresql(df):
    """
    Inserts data into the drone_telemetry table in PostgreSQL.
    Requires that drones and missions exist in the database.
    """
    print("\nConnecting to PostgreSQL...")
    
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        # Verify that drones exist
        cursor.execute("SELECT drone_id FROM drones LIMIT 1")
        if not cursor.fetchone():
            print("No drones in database. Insert drones first.")
            return False
        
        # Verify that missions exist
        cursor.execute("SELECT mission_id FROM missions LIMIT 1")
        if not cursor.fetchone():
            print("No missions in database. Insert missions first.")
            return False
        
        # Filter only valid records
        df_valid = df[df["drone_id"].isin(range(1, NUM_DRONES + 1))]
        
        print(f"Inserting {len(df_valid)} records...")
        
        # Bulk insert
        insert_query = """
            INSERT INTO drone_telemetry 
            (drone_id, mission_id, timestamp, latitude, longitude, 
             altitude, speed, battery_level, temperature, distance_from_start)
            VALUES %s
        """
        
        records = [tuple(x) for x in df_valid.to_numpy()]
        execute_values(cursor, insert_query, records)
        conn.commit()
        
        # Verify
        cursor.execute("SELECT COUNT(*) FROM drone_telemetry")
        total = cursor.fetchone()[0]
        print(f"Inserted. Total in database: {total} records")
        
        return True
        
    except Exception as e:
        print(f"Error: {e}")
        return False
    finally:
        if 'conn' in locals():
            cursor.close()
            conn.close()


def export_to_csv(df, stats):
    """
    Exports data and statistics to CSV for Power BI.
    """
    print("\nExporting to CSV...")
    
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    
    # Data CSV
    data_path = os.path.join(OUTPUT_DIR, "telemetry_data.csv")
    df.to_csv(data_path, index=False)
    print(f"   Data: {data_path}")
    
    # Statistics CSV
    stats_path = os.path.join(OUTPUT_DIR, "telemetry_statistics.csv")
    stats_df = pd.DataFrame(stats).T
    stats_df.to_csv(stats_path)
    print(f"   Statistics: {stats_path}")
    
    # Configured ranges CSV
    ranges_path = os.path.join(OUTPUT_DIR, "configured_ranges.csv")
    ranges_df = pd.DataFrame(RANGES).T
    ranges_df.to_csv(ranges_path)
    print(f"   Ranges: {ranges_path}")


def main():
    print("=" * 70)
    print("DRONE FLEET - TELEMETRY DATA GENERATOR")
    print("=" * 70)
    print()
    
    # 1. Generate data
    df = generate_telemetry_data()
    
    # 2. Calculate statistics
    stats = calculate_statistics(df)
    
    # 3. Export to CSV
    export_to_csv(df, stats)
    
    # 4. Insert into PostgreSQL (optional)
    insert_answer = input("\nInsert into PostgreSQL? (y/n): ").lower()
    if insert_answer == 'y':
        insert_into_postgresql(df)
    
    print("\n" + "=" * 70)
    print("PIPELINE COMPLETED")
    print("=" * 70)
    print(f"\nFiles generated in: {os.path.abspath(OUTPUT_DIR)}")
    print("   - telemetry_data.csv (for Power BI)")
    print("   - telemetry_statistics.csv")
    print("   - configured_ranges.csv")
    print("\nNext step: Import into Power BI")


if __name__ == "__main__":
    main()