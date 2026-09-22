# JPA Repositories and Integration Testing

## 1. Objective

The objective of this development stage was to continue building the persistence layer of the application using Spring Data JPA and to verify the interaction between the Java entities and the PostgreSQL database.

The main focus was the `Drone` entity and its relationship with `DroneModel`.

---

## 2. Implemented Changes

### 2.1 Drone Repository

A `DroneRepository` was created using Spring Data JPA:

```java
public interface DroneRepository extends JpaRepository<Drone, Long> {
}
```


By extending JpaRepository, the application automatically obtains standard persistence operations such as:

Save
Find by ID
Find all
Update
Delete

No custom queries were required at this stage.

## 2.2 Drone and DroneModel Relationship

The relationship between Drone and DroneModel was implemented using JPA.

A drone belongs to one drone model, while the same drone model can be associated with multiple drones.

The relationship was implemented as:

@ManyToOne
@JoinColumn(name = "drone_model_id", nullable = false)
private DroneModel droneModel;

This maps the drone_model_id foreign key in the drones table to the DroneModel entity.

The relationship was chosen instead of storing only the foreign-key value as a Long because the relationship is part of the domain model and should be represented directly in the Java entities.

## 3. Integration Testing

A DroneRepositoryTest was created to verify the persistence of a drone and its relationship with a drone model.

The test performs the following operations:

Creates a DroneModel.
Saves the DroneModel using DroneModelRepository.
Creates a Drone.
Associates the drone with the saved DroneModel.
Saves the Drone using DroneRepository.
Retrieves the drone using its generated ID.
Verifies the stored values.
Verifies the associated DroneModel.

The test uses:

@SpringBootTest
@Transactional

This makes the test an integration test using the Spring application context and a transactional database operation.

##4. Problems Encountered
###4.1 Test Database Configuration

The repository test initially failed because the database environment variables were not correctly configured in the IntelliJ test configuration.

The application uses the following variables:

DB_URL
DB_USERNAME
DB_PASSWORD
Solution

The environment variables were added to the IntelliJ test run configuration.

After this change, the test was able to start correctly and establish a connection with PostgreSQL.

4.2 DroneModel.createdAt Was Null

After fixing the database configuration, the test failed while inserting the DroneModel.

The PostgreSQL database reported a NOT NULL constraint violation for:

drone_models.created_at

The database column is defined with:

created_at TIMESTAMP NOT NULL DEFAULT NOW()

However, Hibernate included created_at in the INSERT statement with a NULL value.

Because Hibernate was explicitly providing the value, the PostgreSQL default was not used.

Solution

A JPA lifecycle callback was added to DroneModel:

@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
}

This initializes the Java field before Hibernate performs the insert.

4.3 Drone.createdAt Was Null

After fixing DroneModel, the test progressed to the insertion of the Drone.

The test then failed because drones.created_at was also receiving a NULL value.

The error was:

ERROR: null value in column "created_at" of relation "drones"
violates not-null constraint
Solution

A @PrePersist lifecycle callback was added to Drone:

@PrePersist
protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();

    createdAt = now;
    updatedAt = now;
}

This initializes both timestamps when a new Drone is persisted.

## 5. Final Result

After applying the previous changes, DroneRepositoryTest passed successfully.

The test confirmed that:

The application can connect to PostgreSQL.
DroneModel can be persisted.
Drone can be persisted.
The Drone to DroneModel relationship works correctly.
The generated drone ID is available after persistence.
The persisted drone can be retrieved from PostgreSQL.
The expected drone and drone model values are correctly stored and retrieved.
## 6. Technical Lessons
PostgreSQL Defaults vs JPA Lifecycle Callbacks

A database default such as:

DEFAULT NOW()

does not guarantee that the value will be generated when Hibernate explicitly sends NULL for that column.

When a value is part of the entity state managed by the application, a JPA lifecycle callback such as @PrePersist can initialize the field before persistence.

Entity Relationships

Foreign keys in the database can be represented as relationships between JPA entities.

For example:

@ManyToOne
@JoinColumn(name = "drone_model_id", nullable = false)
private DroneModel droneModel;

This provides a direct relationship between the Java domain model and the relational database model.

Integration Tests

Repository integration tests are useful for detecting problems that cannot always be identified by compiling the Java code alone.

In this case, the test detected differences between:

The Java entity state.
Hibernate's generated SQL.
PostgreSQL constraints and defaults.
## 7. Status

Completed.

The DroneRepository and its integration test are implemented and passing successfully.

The persistence relationship between Drone and DroneModel has also been verified against PostgreSQL.