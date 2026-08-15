# TrackWise

**TrackWise** is a comprehensive personal asset management system designed to help users organize, track, and manage their valuable assets, subscriptions, payments, and critical documents in one unified platform.

## Project Structure

The project is structured as a multi-module repository:

*   **[`docs/`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/docs/)**: Project proposition, requirements, subsystem architecture, and system modeling definitions.
*   **[`app/backend/`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/app/backend/)**: Java 21 & Spring Boot backend REST API.
*   **[`app/frontend/`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/app/frontend/)**: React, TypeScript, and Vite frontend workspace (boilerplate).

---

## Getting Started

### Prerequisites

*   Docker and Docker Compose
*   Java Development Kit (JDK) 21 (for backend development)
*   Node.js (for frontend development)

### Running the Entire Stack (Local Dev)

From the `app/` directory, run:

```bash
docker compose up --build
```

This starts:
*   **Backend API** at `http://localhost:8080`
*   **Frontend UI** at `http://localhost:5173`
*   **PostgreSQL** at `localhost:5433` (accessible inside the docker network on port 5432)
*   **pgAdmin** at `http://localhost:5050` (credentials: `admin@trackwise.com` / `admin`)

---

## Documentation

*   **Requirements:** The comprehensive Project Proposition and Requirements Document can be found in [`proposal.tex`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/docs/proposal/proposal.tex).
*   **System Modeling:** Diagrams and specifications are located in the [`docs/modeling/`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/docs/modeling/) directory:
    *   [`domain-model.puml`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/docs/modeling/domain-model.puml): Domain concepts and logic.
    *   [`er-diagram.puml`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/docs/modeling/er-diagram.puml): Entity-relationship definitions.
    *   [`openapi.yaml`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/docs/modeling/openapi.yaml): API specification.
    *   [`subsystem-identification.md`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/docs/modeling/subsystem-identification.md): Core subsystems analysis.

---

## Technology Stack

*   **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, Hibernate, JWT.
*   **Database:** PostgreSQL with Flyway database migration scripts.
*   **Frontend:** React, TypeScript, Vite.
*   **CI/CD:** GitHub Actions for formatting, compilation, and testing.

---

## Contribution and Security

*   See [`CONTRIBUTING.md`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/CONTRIBUTING.md) for style conventions, formatting commands, and branch strategies.
*   See [`SECURITY.md`](file:///c:/Users/ASUS/Desktop/eduardo/projects/TrackWise/SECURITY.md) for vulnerability reporting procedures.
