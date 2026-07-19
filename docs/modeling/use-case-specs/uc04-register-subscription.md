# UC-04: Register Subscription

| Field            | Value                 |
| ---------------- | --------------------- |
| **Use Case ID**  | UC-04                 |
| **Name**         | Register Subscription |
| **Actor**        | End User              |
| **Priority**     | High                  |
| **FR Reference** | FR-201, FR-202        |

## Description

An authenticated user registers a new subscription with its recurring payment schedule, optionally linking it to an existing asset.

## Preconditions

- The user is authenticated.

## Main Flow

1. The user navigates to the subscription registration page.
2. The user enters subscription information: name, description, cost per cycle, and currency.
3. The user defines the recurrence rule: frequency (daily, weekly, monthly, quarterly, annually, custom), interval, and start date. _(includes UC: Define Recurrence Rule)_
4. The system calculates and displays the next renewal date based on the recurrence rule.
5. The user submits the registration form.
6. The system validates all input fields.
7. The system creates the subscription with status ACTIVE, associates the recurrence rule, and links it to the user.
8. The system redirects to the new subscription's detail page with a success message.

## Alternative Flows

### AF-1: Link to Asset (extends)

- **Between steps 2 and 3:** The user selects an existing asset to associate with the subscription.
- The system links the subscription to the asset (e.g., car insurance linked to a car asset).

### AF-2: Assign Tags (extends)

- **Between steps 4 and 5:** The user selects existing tags or creates new ones.
- The system associates the tags with the subscription.

### AF-3: Custom Frequency

- **At step 3:** The user selects "CUSTOM" frequency.
- The user specifies a custom interval (e.g., every 2 months, every 6 weeks).
- The system validates the custom interval.

### AF-4: Validation Failure

- **At step 6:** Required fields are missing or invalid (e.g., negative cost, missing frequency).
- The system displays validation errors.
- The user returns to step 2 with previously entered data preserved.

## Postconditions

- A new subscription exists in the system with status ACTIVE.
- A recurrence rule is created and linked to the subscription.
- The next renewal date is calculated and stored.
- If provided: asset link and tags are associated.
