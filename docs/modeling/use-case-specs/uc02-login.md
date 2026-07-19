# UC-02: Login (Authenticate)

| Field            | Value                   |
| ---------------- | ----------------------- |
| **Use Case ID**  | UC-02                   |
| **Name**         | Login                   |
| **Actor**        | End User, Administrator |
| **Priority**     | High                    |
| **FR Reference** | FR-701                  |

## Description

A registered user authenticates with the system using their credentials to access protected resources.

## Preconditions

- The user has a registered account.

## Main Flow

1. The user navigates to the login page.
2. The user enters email and password.
3. The user submits the login form.
4. The system validates the credentials against the stored password hash.
5. The system verifies the account status is ACTIVE.
6. The system generates a JWT access token and a refresh token.
7. The system updates the user's last login timestamp.
8. The system redirects the user to their dashboard (End User) or admin panel (Administrator).

## Alternative Flows

### AF-1: Invalid Credentials

- **At step 4:** The email doesn't exist or the password doesn't match.
- The system displays: "Invalid email or password."
- The user returns to step 2.

### AF-2: Suspended Account

- **At step 5:** The account status is SUSPENDED.
- The system displays: "Your account has been suspended. Contact support."
- The login is denied.

### AF-3: Deleted Account

- **At step 5:** The account status is DELETED.
- The system displays: "Invalid email or password." (same as invalid credentials, for security).
- The login is denied.

## Postconditions

- The user has a valid JWT session token.
- The user's last login timestamp is updated.
- The user is redirected to the appropriate landing page based on their role.
