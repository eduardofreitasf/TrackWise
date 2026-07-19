# UC-05: Register Payment

| Field            | Value                          |
| ---------------- | ------------------------------ |
| **Use Case ID**  | UC-05                          |
| **Name**         | Register Payment               |
| **Actor**        | End User                       |
| **Priority**     | High                           |
| **FR Reference** | FR-301, FR-303, FR-304, FR-305 |

## Description

An authenticated user registers a new payment, which can be standalone, associated with an asset, or associated with a subscription. The payment can be one-time or recurring.

## Preconditions

- The user is authenticated.

## Main Flow

1. The user navigates to the payment registration page.
2. The user enters payment information: amount, currency, due date, and description.
3. The user selects whether the payment is one-time or recurring.
4. The user submits the payment form.
5. The system validates all input fields.
6. The system creates the payment with status PENDING and associates it with the user.
7. The system redirects to the payment detail page with a success message.

## Alternative Flows

### AF-1: Link to Asset (extends)

- **Between steps 2 and 3:** The user selects an existing asset to associate with the payment.
- The system links the payment to the asset.

### AF-2: Link to Subscription (extends)

- **Between steps 2 and 3:** The user selects an existing subscription to associate with the payment.
- The system links the payment to the subscription.

### AF-3: Recurring Payment (extends)

- **At step 3:** The user selects "recurring."
- The user defines a recurrence rule: frequency, interval, start date, and optional end date. _(includes UC: Define Recurrence Rule)_
- The system marks the payment as recurring and associates the recurrence rule.

### AF-4: Assign Category (extends)

- **Between steps 3 and 4:** The user selects an existing category (PAYMENT scope) or creates a new one.
- The system associates the category with the payment.

### AF-5: Assign Tags (extends)

- **Between steps 3 and 4:** The user selects existing tags or creates new ones.

### AF-6: Attach Document (extends)

- **Between steps 3 and 4:** The user uploads a receipt or invoice.
- See UC-06 for document upload details.

### AF-7: Mark as Already Paid

- **At step 3:** The user indicates the payment has already been made.
- The user enters the payment date.
- The system creates the payment with status PAID instead of PENDING.

### AF-8: Validation Failure

- **At step 5:** Required fields are missing or invalid.
- The system displays validation errors.
- The user returns to step 2 with previously entered data preserved.

## Postconditions

- A new payment exists in the system with status PENDING (or PAID if AF-7).
- The payment is associated with the authenticated user.
- If provided: asset link, subscription link, recurrence rule, category, tags, and documents are associated.
