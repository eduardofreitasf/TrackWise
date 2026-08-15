---
trigger: always_on
---
# TrackWise -- Domain Rules

## 1. Domain Overview & Purpose
TrackWise is a Personal Asset Management System. It allows users to centralize, organize, and monitor assets (e.g. cars, properties, appliances), subscriptions (recurring service renewals), payments (recurring or one-time, standalone or linked), and documents (insurance policies, receipts, warranties) in a single, secure platform.

## 2. Key Domain Entities & Relationships
* **User**: The account owner. All user-created data (assets, subscriptions, payments, documents, categories, tags, notifications, notification preferences) belongs to a specific user and must be strictly isolated.
* **Asset**: A valuable item owned by a user. Can have associated payments, documents, or subscriptions. Supports status transitions (`ACTIVE`, `ARCHIVED`, `SOLD`, `DISPOSED`).
* **Subscription**: A recurring service subscription. Optionally linked to an asset, generates payments, and follows a `RecurrenceRule`. Supports status (`ACTIVE`, `PAUSED`, `CANCELLED`).
* **Payment**: A financial transaction representing an expense. Can be one-time or recurring, standalone, or linked to an asset or subscription. Follows a `RecurrenceRule` if recurring.
* **Document**: A digital file attachment. Polymorphically linked to an asset, payment, or subscription via nullable foreign keys, or standalone at user-level.
* **RecurrenceRule**: Defines frequency (`DAILY`, `WEEKLY`, `MONTHLY`, `QUARTERLY`, `ANNUALLY`, `CUSTOM`), interval, start date, end date, and next occurrence calculation.
* **Category**: User-defined classification hierarchical tree (via self-referencing parent) scoped to either `ASSET` or `PAYMENT`.
* **Tag**: Custom user-defined tag for multi-dimensional labeling.
* **Notification**: In-app or email alert representing triggers like payment due dates, subscription renewals, or document expirations.

## 3. Data Isolation & Security Boundaries
* **Strict Multi-Tenancy**: Data must be completely isolated between users. Under no circumstances should any user see, retrieve, update, or delete another user's assets, payments, subscriptions, categories, tags, or documents. All queries must filter by `userId` (or current authenticated user email).
* **Administrative Isolation**: System administrators can manage user accounts and view system-level statistics, but must never have access to user asset, payment, subscription, or document content.

## 4. Lifecycle & Deletion Rules
* **Soft Delete**: All main domain entities (User, Asset, Subscription, Payment, Document, Category, Tag, Notification) utilize a soft-delete mechanism via the `deleted_at` timestamp. Queries must exclude soft-deleted records.
* **Audit Logging**: Any administrative changes or user status changes must be logged in the immutable `audit_log` table (no soft deletes for audit logs).
