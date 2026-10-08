# Drone Fleet Platform - Initialization Guide

This guide will help you set up and run the Drone Fleet Platform on your local machine.

## Prerequisites

Before starting, ensure you have the following installed:

- **Java 21** (OpenJDK or Temurin)
- **Python 3.10+**
- **Maven 3.9+**
- **Docker & Docker Compose**
- **Git**

## Quick Start (Recommended)

The easiest way to run the entire platform is using Docker Compose.

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/drone-fleet-platform.git
cd drone-fleet-platform
```

### 2. Configure environment variables

```bash
cp .env.example .env
```

Edit `.env` and set your desired credentials:

```env
POSTGRES_DB=drone_fleet
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password
```

### 3. Start all services

```bash
docker-compose up --build
```

This will start:
- **PostgreSQL** on port `5433` (mapped from container port 5432)
- **Redis** on port `6379`
- **Spring Boot Application** on port `8080`

### 4. Verify the setup

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Web Dashboard:** http://localhost:8080/index.html

### 5. Stop the services

```bash
docker-compose down
```

To also remove the database data:

```bash
docker-compose down -v
```

---

## Manual Setup (Alternative)

If you prefer to run services individually without Docker:

### 1. Set up PostgreSQL

Install PostgreSQL 16 and create the database:

```bash
sudo apt install postgresql
sudo -u postgres psql
```

```sql
CREATE DATABASE drone_fleet;
CREATE USER postgres WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE drone_fleet TO postgres;
\q
```

### 2. Set up Redis

Install Redis:

```bash
sudo apt install redis-server
sudo systemctl start redis
```

### 3. Configure Spring Boot

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edit `application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/drone_fleet
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### 4. Run the backend

```bash
mvn spring-boot:run
```

### 5. Configure and run the Python scripts

```bash
cd scripts
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
cp config.example.py config.py
```

Edit `config.py` with your database credentials, then run:

```bash
python generate_data.py
```
## IMPORTANT: Running the ML Microservice (Requires Two Terminals)

The Machine Learning prediction feature runs as a separate Python microservice. To use the "ML Predictions" tab in the web dashboard, you **must keep two terminals open simultaneously**: one for the Spring Boot backend and one for the Flask ML service.

**Terminal 1 - Spring Boot Backend** (Keep this running in the background):

    mvn spring-boot:run

**Terminal 2 - Flask ML Microservice** (Open a new terminal tab or window):

    cd scripts
    source venv/bin/activate
    python ml_service.py

Expected output in Terminal 2:

    Loading ML model...
    Model loaded successfully.
    Starting ML Prediction Service on port 5000...
     * Running on http://127.0.0.1:5000

Once both services are running, open your browser at http://localhost:8080/index.html, navigate to the **ML Predictions** tab, fill in the flight parameters, and click **Predict Battery Level**.

Note: In a production environment or when using Docker Compose in the future, this manual step is eliminated as all services are orchestrated together.

---

## Seeding the Database

To populate the database with sample data (drones, missions, telemetry):

```bash
cd scripts
source venv/bin/activate
python generate_data.py
```

When prompted:
- Insert into PostgreSQL? → `y`
- Update Redis with real-time state? → `y`

This will:
- Generate 10,000+ telemetry records
- Insert them into PostgreSQL (historical data)
- Update Redis with the latest drone states (real-time data)
- Export CSV files to `scripts/output/` for Power BI

---

## Testing the ML Predictions

1. Ensure the ML microservice is running (`python ml_service.py`)
2. Open the web dashboard: http://localhost:8080/index.html
3. Navigate to **ML Predictions**
4. Fill in flight parameters and click **Predict Battery Level**

Example values:
- Speed: `45` m/s
- Altitude: `80` m
- Temperature: `25` °C
- Distance: `1500` m

---

## Troubleshooting

### Port already in use

If you see "Address already in use" errors:
- PostgreSQL: Change the host port in `docker-compose.yml` (e.g., `5433:5432`)
- Redis: Stop any local Redis instance (`sudo systemctl stop redis`)
- Spring Boot: Kill the process on port 8080 (`lsof -ti:8080 | xargs kill`)

### Database connection refused

Ensure PostgreSQL is running and accepting connections:

```bash
# With Docker
docker-compose ps

# Manual install
sudo systemctl status postgresql
```

### Redis connection refused

Ensure Redis is running:

```bash
# With Docker
docker-compose ps

# Manual install
redis-cli ping  # Should return PONG
```

### ML service unavailable

Ensure the Flask microservice is running in a separate terminal:

```bash
cd scripts
source venv/bin/activate
python ml_service.py
```

---

## Project Structure Overview

```
drone-fleet-platform/
├── src/main/java/          # Spring Boot backend
├── src/main/resources/
│   ├── static/             # Web dashboard (HTML/JS/CSS)
│   └── application.properties.example
├── scripts/                # Python data pipeline & ML
│   ├── generate_data.py    # Telemetry generator
│   ├── train_model.py      # ML training pipeline
│   ├── ml_service.py       # Flask inference service
│   └── config.example.py
├── docs/                   # Documentation
├── docker-compose.yml      # Container orchestration
├── Dockerfile              # Spring Boot container
└── README.md
```

## Need Help?

- Check the [README](../README.md) for project overview
- Review the [API Documentation](http://localhost:8080/swagger-ui.html)
- Open an issue on [GitHub](https://github.com/Psosa3232/drone-fleet-platform/issues)

