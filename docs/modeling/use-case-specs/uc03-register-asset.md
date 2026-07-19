# UC-03: Register Asset

| Field            | Value          |
| ---------------- | -------------- |
| **Use Case ID**  | UC-03          |
| **Name**         | Register Asset |
| **Actor**        | End User       |
| **Priority**     | High           |
| **FR Reference** | FR-101, FR-105 |

## Description

An authenticated user registers a new asset in the system with its metadata, optionally assigning a category, tags, and attaching documents.

## Preconditions

- The user is authenticated.

## Main Flow

1. The user navigates to the asset registration page.
2. The user enters asset information: name, description, purchase date, purchase price, and currency.
3. The system displays the user's existing categories (filtered to ASSET scope) and tags.
4. The user submits the registration form.
5. The system validates all input fields.
6. The system creates the asset with status ACTIVE and associates it with the user.
7. The system redirects to the new asset's detail page with a success message.

## Alternative Flows

### AF-1: Assign Category (extends)

- **Between steps 3 and 4:** The user selects an existing category or creates a new one.
- The system associates the category with the asset.

### AF-2: Assign Tags (extends)

- **Between steps 3 and 4:** The user selects existing tags or creates new ones.
- The system associates the tags with the asset.

### AF-3: Attach Document (extends)

- **Between steps 3 and 4:** The user uploads one or more documents (receipts, registration, etc.).
- The system stores the documents and associates them with the asset.
- See UC-06 for document upload details.

### AF-4: Validation Failure

- **At step 5:** Required fields are missing or invalid (e.g., negative price, future purchase date beyond today).
- The system displays validation errors.
- The user returns to step 2 with previously entered data preserved.

## Postconditions

- A new asset exists in the system with status ACTIVE.
- The asset is associated with the authenticated user.
- If provided: category, tags, and documents are associated with the asset.
