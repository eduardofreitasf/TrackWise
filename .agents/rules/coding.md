---
trigger: always_on
---
# TrackWise -- Coding Rules

## 1. Class & Method Structure
* **Single Responsibility**: Keep controllers, services, and repositories focused on a single responsibility. Controllers must handle only REST mappings, DTO serialization, and request/response coordination. Core business logic, status validations, and queries must reside in Service classes.
* **Method Size**: Keep methods under ~40 lines. Split complex methods into helper functions or separate domain services.
* **Early Returns (Guard Clauses)**: Limit nesting depth to a maximum of 2 levels by using early returns/guard clauses to handle validation or error conditions.

## 2. Spring Boot & Database Standards
* **Data Auditing**: All entity classes must extend `BaseEntity` to inherit automatic tracking of `id`, `createdAt`, `updatedAt`, and soft-delete `deleted_at`.
* **Soft-Delete Handling**: Ensure queries filter out soft-deleted records (`deletedAt IS NULL`).
* **Avoid N+1 Queries**: Use proper entity joins, entity graphs, or fetch joins in Spring Data JPA repositories to load associated relations when needed, instead of querying database records inside loops.
* **Data Validation**: Validate all incoming DTOs using Jakarta Validation annotations (e.g. `@NotNull`, `@Size`, `@Email`).

## 3. Error Handling & API Integrity
* **Standardized Exception Handling**: Never return raw exception stack traces. Let exceptions bubble up to `GlobalExceptionHandler` to format standard RFC 7807 problem details or custom error structures.
* **No Silent Failures**: Never write empty `catch` blocks. Log warnings or errors using structured logging and map exceptions to appropriate custom business exceptions (`ResourceNotFoundException`, `BusinessException`).
* **Entity Existence Guards**: Always verify that an entity exists and belongs to the authenticated user before executing update, delete, or retrieve mutations, returning a 404 response if not found.

## 4. Code Style & Cleanliness
* **Spotless Checking**: Run Spotless formatting (`mvn spotless:apply`) before committing. All code must conform to the project's formatting configuration.
* **No Console Logs/Prints**: Remove all debug outputs (`System.out.println` or raw prints) before completing tasks. Use SLF4J logger for logging.
* **No Dangling TODOs**: Do not leave dangling TODO comments in code. Fix them or link them to a tracked repository issue.
