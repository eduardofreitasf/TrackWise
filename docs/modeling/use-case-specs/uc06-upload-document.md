# UC-06: Upload Document

| Field            | Value                  |
| ---------------- | ---------------------- |
| **Use Case ID**  | UC-06                  |
| **Name**         | Upload Document        |
| **Actor**        | End User               |
| **Priority**     | High                   |
| **FR Reference** | FR-401, FR-402, FR-403 |

## Description

An authenticated user uploads a document to the system, optionally associating it with an asset, payment, or subscription. Documents can also be standalone (user-level).

## Preconditions

- The user is authenticated.

## Main Flow

1. The user navigates to the document upload page (or triggers upload from an asset/payment/subscription detail page).
2. The user selects one or more files to upload.
3. The user enters a name and optional description for each document.
4. The user optionally sets an expiration date (e.g., for insurance policies, warranties).
5. The user submits the upload form.
6. The system validates the files (type, size limits).
7. The system stores the files in the configured storage backend.
8. The system creates document records with metadata (name, file path, MIME type, file size).
9. The system redirects to the document detail page with a success message.

## Alternative Flows

### AF-1: Associate with Asset

- **Between steps 3 and 4:** The user selects an existing asset to associate with the document.
- The system links the document to the asset.

### AF-2: Associate with Payment

- **Between steps 3 and 4:** The user selects an existing payment.
- The system links the document to the payment.

### AF-3: Associate with Subscription

- **Between steps 3 and 4:** The user selects an existing subscription.
- The system links the document to the subscription.

### AF-4: Upload from Entity Detail Page

- **At step 1:** The user triggers upload from an asset, payment, or subscription detail page.
- The association is automatically set to the parent entity.
- The flow continues from step 2.

### AF-5: File Validation Failure

- **At step 6:** The file type is not supported or exceeds size limits.
- The system displays: "File type not supported" or "File exceeds maximum size."
- The user returns to step 2.

### AF-6: Storage Failure

- **At step 7:** The storage backend is unavailable or the upload fails.
- The system displays: "Upload failed. Please try again."
- The user returns to step 2.

## Postconditions

- One or more document records exist in the system, linked to the user.
- Files are stored in the configured storage backend.
- If provided: association with asset, payment, or subscription is established.
- If an expiration date is set, the document is eligible for expiration alerts.
