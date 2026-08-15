---
name: run-spotless
description: >-
  Use this skill to apply code formatting and clean up code style in the Java backend module using Spotless.
---

# Run Spotless Formatting

This skill provides the necessary command sequence to format all Java source files in the project backend using the configured Spotless configuration.

## Steps

1. Locate the backend directory:
   `app/backend`
2. Run the Spotless apply command:
   `mvn spotless:apply`
3. Verify that the spotless task completed successfully. If formatting errors persist or the check fails, inspect the console output.
