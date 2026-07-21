# TrackWise Backend API

This is the backend service for the TrackWise Personal Asset Management System, built with Spring Boot, Java 21, and PostgreSQL.

## Developer Cheatsheet

Below is a complete list of Maven commands you will use during backend development. All commands should be run using the included `./mvnw` (Maven Wrapper) from this `backend/` directory.

### 1. Running the Application Locally

If you have the database running (via the root `docker-compose.yml`), you can start the Spring Boot server (runs on `http://localhost:8080`):

```bash
./mvnw spring-boot:run
```

### 2. Code Formatting (Spotless)

We use the Spotless plugin with the Google Java Format (AOSP style). The build will fail if files are unformatted.

**Check formatting:**

```bash
./mvnw spotless:check
```

**Apply formatting automatically (Run this before committing!):**

```bash
./mvnw spotless:apply
```

### 3. Running Tests

Run all unit and integration tests (Testcontainers will automatically spin up a temporary database if configured):

```bash
./mvnw test
```

### 4. Code Coverage (JaCoCo)

The JaCoCo plugin automatically attaches to the `test` phase. To generate the code coverage report, simply run tests:

```bash
./mvnw clean test
```

**View the Report:** Open `target/site/jacoco/index.html` in your web browser.

### 5. Building the Application

To compile the code, format it, run tests, and package it into an executable JAR file:

```bash
./mvnw clean package
```

_(The generated JAR will be located in the `target/` directory)._

If you want to build the JAR quickly **without running tests**:

```bash
./mvnw clean package -DskipTests
```

### 6. Database Migrations (Flyway)

Flyway migrations are executed automatically when the application starts or when tests run.

- Migration files are located in `src/main/resources/db/migration`.
- Naming convention: `V1__Description.sql`, `V2__Description.sql`.

### 7. API Documentation (Swagger/OpenAPI)

_(Note: Springdoc needs to be added as a dependency first before this works)_
Once enabled, the live interactive API documentation will be available at:
`http://localhost:8080/swagger-ui.html`
