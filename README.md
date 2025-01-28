# StatementProcessor

## Overview

The **StatementProcessor** is a microservice that processes customer statements, validates the data, and sends events for further processing. The service checks for:
- Uniqueness of transaction references.
- Validates the end balance of the statements.

If any records fail validation, the application generates a report that includes the transaction references and descriptions of the failed records.

### Features:
- **Java 21** and **Spring Boot 3.3.x** for the backend services.
- **Maven** for dependency management and build lifecycle.
- **Docker** for containerization.
- **Flyway** for database migrations.
- **H2 Database** used for local development and testing.
- **Jackson** for handling CSV and XML formats.
- **JUnit 5**, **Mockito**, and **AssertJ** for unit testing.
- **Swagger** for API documentation.
- **Logback** for logging with color configuration.
- **Spring Events** to handle statement-related events.

### Requirements:
- **Java 21**
- **Maven**
- **Docker** for containerization
- **H2 Database** for local testing (or any other preferred database).
- **Spring Boot 3.3.*** and associated dependencies.

## Running the Application

### 1. Running Locally

To run the application locally, make sure you have the following installed:
- Java 21
- Maven
- Docker

Once you've cloned the repository, you can build the application with:

```bash
./mvnw clean install
```
To run the test use:
```bash
./mvnw test
```

To run the application use:
```bash
./mvnw spring-boot:run
```

This will start the application on `http://localhost:8080`.

### 2. Building and Running with Docker

You can also run the application in Docker containers.

#### Build the Docker image:
```bash
docker build -t statementprocessor:latest .
```

#### Run the Docker container:
```bash
docker run -p 8080:8080 statementprocessor:latest
```

This will run the application in a Docker container and map port 8080 on your host machine to port 8080 on the container.

#### Swagger API Documentation

You can access the Swagger UI to interact with the API at:

```bash
http://localhost:8080/
```

Alternatively, you can use tools like **Postman** to test the API endpoints.

## Features Implemented

- **Statement Event:** An event system is integrated to send statements to a database after processing.
- **Validation:** Ensures all transaction references are unique and the end balance is validated.
- **Database Support:** Flyway for migrations and H2 for local testing.
- **Logging:** Logback is configured for colored logs.
- **Testing:** JUnit, Mockito, and AssertJ are used for unit testing.

## Features Not Yet Implemented

- **Spring Security with OAuth** for securing API endpoints.
- **Retrieving saved statements from the database**.
- **Saving and retrieving validation reports**.
- **Deployment to Kubernetes** with complete configurations.
- **GitLab CI** configurations for continuous integration and deployment are not fully set up.
- **Monitoring** with **Prometheus** and **Grafana** dashboards has not been implemented.
- **Distributed tracing** with **Jaeger** is not configured yet.

## Testing

### Running Unit Tests

To run unit tests, use Maven:

```bash
./mvnw test
```

### Running with Maven Build

To build the project and run the tests, use:

```bash
./mvnw clean install
```

This will compile the code, run the tests, and package the application into a JAR file.

### Build and Package the Application

To package the application into a JAR file:

```bash
./mvnw clean package
```

This will generate the `target/statementprocessor-0.0.1-SNAPSHOT.jar` file.


## Docker Configuration

The Dockerfile and the `.dockerignore` file are configured to package and run the application in a Docker container.

- **Dockerfile**:
    - Based on `eclipse-temurin:21-jre-alpine` for a lightweight image, with minimal size, use multi-stage builds.
    - Build application using `maven:3.9.4-eclipse-temurin-21`.
    - Copies the application JAR file and runs it.

## CI/CD Setup

The GitLab CI file is prepared for continuous integration and deployment but is not fully 
configured due to lack of personal environments.

## Kubernetes Deployment
The application is partially configured for Kubernetes `/k8s` folder.

### Kubernetes YAML file:
- **Deployment YAML** - Defines how the application will be deployed in Kubernetes.
- **Service YAML** - Exposes the application as a service within the Kubernetes cluster.
- **HorizontalPodAutoscaler** - Can be added to scale the application based on load.

## Monitoring and Tracing

- **Prometheus** and **Grafana** dashboards are planned but not implemented yet.
- **Distributed tracing with Spring Cloud Sleuth and Jaeger** is also planned for future implementation.

## Further Improvements

- **Security:** OAuth-based security should be implemented for API protection.
- **Database:** Support for retrieving and saving validation reports should be added.
- **Kubernetes:** Complete deployment configurations need to be created for Kubernetes.

## Conclusion

This microservice provides a basic framework for statement processing, including transaction reference validation, balance validation, and generating reports for failed records. It is packaged with Docker for easy deployment and has several features for further enhancement.
