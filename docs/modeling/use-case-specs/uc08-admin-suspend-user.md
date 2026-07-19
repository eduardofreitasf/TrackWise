# UC-08: Admin — Suspend User Account

| Field            | Value                |
| ---------------- | -------------------- |
| **Use Case ID**  | UC-08                |
| **Name**         | Suspend User Account |
| **Actor**        | Administrator        |
| **Priority**     | Medium               |
| **FR Reference** | FR-802, FR-804       |

## Description

An administrator suspends a user account due to a terms of service violation or other administrative reason. The action is logged in the audit trail.

## Preconditions

- The administrator is authenticated with admin privileges.
- The target user account exists and has status ACTIVE.

## Main Flow

1. The administrator navigates to the user management page.
2. The system displays a list of user accounts with: email, name, status, creation date, and last login date.
3. The administrator selects a user account.
4. The system displays user account details (metadata only — no access to user's assets, payments, or documents).
5. The administrator selects the "Suspend Account" action.
6. The administrator enters a reason for suspension.
7. The administrator confirms the suspension.
8. The system updates the user's status to SUSPENDED.
9. The system creates an audit log entry: actor (admin), action (SUSPEND_USER), target user ID, reason, timestamp, and IP address.
10. The system sends a notification to the suspended user (email) informing them of the suspension.
11. The system displays a confirmation message.

## Alternative Flows

### AF-1: User Already Suspended

- **At step 4:** The target user's status is already SUSPENDED.
- The "Suspend Account" action is disabled.
- The administrator sees the option to "Reactivate" instead.

### AF-2: User is Deleted

- **At step 4:** The target user's status is DELETED.
- The "Suspend Account" action is disabled.
- No actions are available for deleted accounts.

### AF-3: Admin Cancels

- **At step 7:** The administrator cancels the action.
- No changes are made. The system returns to the user detail view.

## Postconditions

- The target user's status is SUSPENDED.
- An audit log entry is created with the admin's identity, action, reason, and timestamp.
- The suspended user's existing JWT sessions remain valid until expiry, but they cannot log in again.
- The user receives an email notification about the suspension.
