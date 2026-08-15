---
name: db-migration-create
description: >-
  Use this skill when the user requests a database schema change or when a new Flyway migration file needs to be created in the Java backend.
---

# DB Migration Create

This skill outlines the process of adding a new Flyway database migration to the Java/Spring Boot backend project.

## Steps

1. Locate the migration directory:
   `app/backend/src/main/resources/db/migration/`
2. Identify the next migration version number by examining the existing migration files (e.g., if `V1__init_schema.sql` exists, the next is `V2`).
3. Create a new SQL file named:
   `V<Version>__<Description>.sql` (e.g., `V2__add_payment_table.sql`, with a double underscore after the version).
4. Write SQL commands complying with the PostgreSQL dialect.
5. Compile and test the application to verify configuration and schema validation:
   `mvn clean compile`
