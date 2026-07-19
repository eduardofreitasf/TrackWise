# UC-01: Register Account

| Field            | Value                   |
| ---------------- | ----------------------- |
| **Use Case ID**  | UC-01                   |
| **Name**         | Register Account        |
| **Actor**        | End User (unregistered) |
| **Priority**     | High                    |
| **FR Reference** | FR-701                  |

## Description

A new user creates an account in the system by providing personal information and credentials.

## Preconditions

- The user does not have an existing account with the same email.

## Main Flow

1. The user navigates to the registration page.
2. The user enters first name, last name, email, password, and password confirmation.
3. The user selects a default currency.
4. The user submits the registration form.
5. The system validates all input fields (format, password strength, email uniqueness).
6. The system creates the user account with status ACTIVE.
7. The system creates default notification preferences for the user.
8. The system redirects the user to the login page with a success message.

## Alternative Flows

### AF-1: Email Already Registered

- **At step 5:** The system detects the email is already in use.
- The system displays an error message: "An account with this email already exists."
- The user returns to step 2.

### AF-2: Password Validation Failure

- **At step 5:** The password does not meet strength requirements or confirmation doesn't match.
- The system displays specific validation errors.
- The user returns to step 2.

### AF-3: Invalid Input

- **At step 5:** One or more fields are empty or have invalid format.
- The system highlights invalid fields with error messages.
- The user returns to step 2.

## Postconditions

- A new user account exists in the system with status ACTIVE.
- Default notification preferences are created for the user.
- The user can now log in with their credentials.
