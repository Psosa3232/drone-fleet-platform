# Service Layer

## Purpose

The service layer contains the application's business logic.

It acts as an intermediate layer between the REST controllers
and the persistence layer.

The main responsibility of the service layer is to apply
business rules before accessing or modifying persistent data.

## Architecture

The current application flow is:

```
Controller
↓
Service
↓
Repository
↓
PostgreSQL
```

## Dependency Injection

Services depend on repositories to access persistent data.

Spring manages these dependencies through dependency injection.

For example, `DroneService` receives a `DroneRepository`
through its constructor.

```java
public DroneService(DroneRepository droneRepository) {
    this.droneRepository = droneRepository;
}
```


This keeps the service independent of the concrete creation
of repository objects.

## DroneService

The first service implemented in the application is DroneService.

The first operation is:

```
getDroneById()
```


Its responsibility is to retrieve a drone by its identifier.

If the drone exists, the service returns the corresponding
Drone entity.

If the drone does not exist, the service throws
```
DroneNotFoundException
```

## Exception Handling

The application defines a custom exception:

```
DroneNotFoundException
```

This exception is used when a requested drone cannot be found.

The repository returns an Optional<Drone> from findById().

The service interprets an empty Optional as a business-level
"drone not found" condition and converts it into
DroneNotFoundException.

## Testing

DroneServiceTest verifies two scenarios:

A drone can be retrieved successfully by its identifier.
DroneNotFoundException is thrown when the requested drone
does not exist.

The tests use Spring Boot integration testing and PostgreSQL
to verify the complete persistence flow.

Current Status

Implemented:

DroneService
Constructor dependency injection
getDroneById()
DroneNotFoundException
Integration tests for successful retrieval
Integration tests for missing drones

Next:

Implement getAllDrones()


