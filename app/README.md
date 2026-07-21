# TrackWise Monorepo

Welcome to the TrackWise Personal Asset Management System. This repository is structured as a monorepo containing both the frontend and backend applications.

## Project Structure

- `/backend` - Spring Boot (Java 21) RESTful API.
- `/frontend` - Vite + React (TypeScript) Single Page Application.
- `docker-compose.yml` - Master orchestration file for local development.

## Prerequisites

- **Docker & Docker Compose** (for running the full stack)
- **Java 21+** (if running the backend manually)
- **Node.js 22+** (if running the frontend manually)

---

## Running the Full Stack (Docker)

The easiest way to develop and run the application is using Docker. We have configured it to hot-reload both the frontend and backend whenever you make code changes in your IDE!

From this root `app/` directory, run:

```bash
sudo docker compose up --build
```

### Services

Once the containers are up and running, you can access the following services:

- **Frontend Application:** [http://localhost:5173](http://localhost:5173)
- **Backend API:** [http://localhost:8080](http://localhost:8080)
- **Database (PostgreSQL):** `localhost:5433`
- **Database Admin UI (pgAdmin):** [http://localhost:5050](http://localhost:5050) _(Login: admin@trackwise.com / admin)_

### Shutting Down

To gracefully stop the containers:

```bash
sudo docker compose down
```

_(Add `--remove-orphans` if you have recently changed service names)._

---

## Manual Development

If you prefer to run the services individually on your host machine (without Dockerizing the apps), you still need to start the database container:

```bash
sudo docker compose up -d postgres pgadmin
```

Then, you can follow the instructions in the respective sub-directories:

- [Backend Instructions](./backend/README.md)
- [Frontend Instructions](./frontend/README.md)
