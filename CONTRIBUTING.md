# Contributing Guidelines

Thank you for contributing to TrackWise! Please review the guidelines below to ensure a smooth contribution process.

## Branch Strategy

*   Use feature branches branched from `development` (e.g., `feat/auth-integration` or `fix/jwt-expiration`).
*   Submit pull requests targeting the `development` branch.
*   Never merge directly into the `main` or `master` branches.

## Commit Messages

We strictly follow [Conventional Commits](https://www.conventionalcommits.org/):

*   `feat`: A new feature (e.g., `feat(auth): add login endpoint`)
*   `fix`: A bug fix (e.g., `fix(asset): resolve null pointer in depreciation`)
*   `docs`: Documentation changes (e.g., `docs(api): update openapi spec`)
*   `style`: Formatting, missing semi-colons, etc. (no production code changes)
*   `refactor`: Code change that neither fixes a bug nor adds a feature

## Development Workflow (Backend)

1.  **Run formatting check**: Ensure Spotless compliance prior to committing:
    ```bash
    mvn spotless:apply
    ```
2.  **Run tests**: Ensure all tests compile and pass:
    ```bash
    mvn clean verify
    ```
3.  **Check for warnings**: Keep compilation clean and avoid adding dangling TODOs.
