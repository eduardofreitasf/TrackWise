# TrackWise — Step 5: Sub-System Identification

## Methodology

Sub-systems are identified by analyzing the use case specifications (Step 3) and domain model (Step 2) to find natural clusters of related functionality. The criteria for grouping are:

1. **Cohesion** — use cases that operate on the same domain entities belong together
2. **Coupling** — minimize dependencies between sub-systems
3. **Ownership** — each entity should be owned by exactly one sub-system
4. **Autonomy** — each sub-system should be independently understandable

---

## Identified Sub-Systems

### SS-01: Authentication & Authorization

| Property                | Value                                                         |
| ----------------------- | ------------------------------------------------------------- |
| **Responsibility**      | User authentication, session management, security enforcement |
| **Owned Entities**      | _(none — uses User entity from SS-02)_                        |
| **Use Cases**           | UC-02 Login, Logout                                           |
| **Interfaces Provided** | JWT token issuance, token validation, current user resolution |
| **Dependencies**        | SS-02 User Management (credential verification)               |

**Rationale:** Separated from User Management because authentication is a cross-cutting concern consumed by every other sub-system. It provides the security infrastructure (JWT filters, authorization checks) that all controllers depend on.

---

### SS-02: User Management

| Property                | Value                                                                   |
| ----------------------- | ----------------------------------------------------------------------- |
| **Responsibility**      | User registration, profile management, account lifecycle                |
| **Owned Entities**      | **User**                                                                |
| **Use Cases**           | UC-01 Register Account, Manage Profile, Change Password, Delete Account |
| **Interfaces Provided** | User CRUD, user lookup by ID/email, account status checks               |
| **Dependencies**        | SS-07 Notification (create default preferences on registration)         |

**Rationale:** Owns the User entity and all self-service account operations. Distinct from Authentication because it deals with user data lifecycle, not session/token management.

---

### SS-03: Asset Management

| Property                | Value                                                                                                                                                                          |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Responsibility**      | Asset registration, lifecycle tracking, history, future commitments                                                                                                            |
| **Owned Entities**      | **Asset**                                                                                                                                                                      |
| **Use Cases**           | UC-03 Register Asset, View Asset Details, View Asset History, View Future Commitments, Update Asset Status                                                                     |
| **Interfaces Provided** | Asset CRUD, asset listing/filtering, asset status transitions                                                                                                                  |
| **Dependencies**        | SS-01 Auth (authorization), SS-06 Classification (categories/tags), SS-05 Document (attachments), SS-04 Payment (asset payment history), SS-08 Scheduling (future commitments) |

**Rationale:** Core business sub-system. Assets are the central concept around which payments, subscriptions, and documents revolve. The history view aggregates payment data, so it has a read dependency on SS-04.

---

### SS-04: Payment Management

| Property                | Value                                                                                                                                                                               |
| ----------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Responsibility**      | Payment registration, recurring scheduling, status tracking, history                                                                                                                |
| **Owned Entities**      | **Payment**                                                                                                                                                                         |
| **Use Cases**           | UC-05 Register Payment, View Payment History, Schedule Recurring Payment, Update Payment Status                                                                                     |
| **Interfaces Provided** | Payment CRUD, payment listing/filtering, payment status transitions, upcoming payments query                                                                                        |
| **Dependencies**        | SS-01 Auth, SS-03 Asset (optional link), SS-10 Subscription (optional link), SS-06 Classification (categories/tags), SS-05 Document (receipts), SS-08 Scheduling (recurrence rules) |

**Rationale:** Payments are the most connected entity — they can link to assets, subscriptions, or stand alone. The sub-system handles both one-time and recurring payment logic.

---

### SS-05: Document Management

| Property                  | Value                                                                                                      |
| ------------------------- | ---------------------------------------------------------------------------------------------------------- |
| **Responsibility**        | Document upload, storage, organization, retrieval, expiration tracking                                     |
| **Owned Entities**        | **Document**                                                                                               |
| **Use Cases**             | UC-06 Upload Document, Search Documents, View Document Details                                             |
| **Interfaces Provided**   | Document upload, document listing/search, document retrieval, expiring documents query                     |
| **External Dependencies** | File Storage backend (filesystem, S3, etc.)                                                                |
| **Dependencies**          | SS-01 Auth, SS-03 Asset (optional link), SS-04 Payment (optional link), SS-10 Subscription (optional link) |

**Rationale:** Manages the full document lifecycle including file storage abstraction. The polymorphic association (documents can belong to assets, payments, or subscriptions) is handled here with nullable FK references.

---

### SS-06: Classification

| Property                | Value                                                                             |
| ----------------------- | --------------------------------------------------------------------------------- |
| **Responsibility**      | Category and tag management, assignment to entities                               |
| **Owned Entities**      | **Category**, **Tag**, junction tables (asset_tag, subscription_tag, payment_tag) |
| **Use Cases**           | Manage Categories, Manage Tags, Assign Category, Assign Tags                      |
| **Interfaces Provided** | Category CRUD, Tag CRUD, category/tag assignment, filtering by category/tag       |
| **Dependencies**        | SS-01 Auth                                                                        |

**Rationale:** Extracted as a shared sub-system because categories and tags are used across Asset, Payment, and Subscription sub-systems. Centralizing avoids duplicating CRUD logic for tags/categories in each consuming sub-system.

---

### SS-07: Notification

| Property                  | Value                                                                                                                   |
| ------------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| **Responsibility**        | Notification generation, delivery, preference management                                                                |
| **Owned Entities**        | **Notification**, **NotificationPreference**                                                                            |
| **Use Cases**             | UC-09 Process Notification Triggers, View Notifications, Configure Preferences, Send Email Notification                 |
| **Interfaces Provided**   | Notification listing, mark as read, preference CRUD                                                                     |
| **External Dependencies** | Email/SMTP service                                                                                                      |
| **Dependencies**          | SS-01 Auth, SS-04 Payment (due date queries), SS-10 Subscription (renewal queries), SS-05 Document (expiration queries) |

**Rationale:** The notification engine is a scheduled, background sub-system that reads from multiple other sub-systems to evaluate triggers. It's clearly separate because it has its own entities, its own scheduling logic, and an external dependency (email).

---

### SS-08: Scheduling

| Property                | Value                                                                        |
| ----------------------- | ---------------------------------------------------------------------------- |
| **Responsibility**      | Recurrence rule management, next occurrence calculation                      |
| **Owned Entities**      | **RecurrenceRule**                                                           |
| **Use Cases**           | Define Recurrence Rule (shared/included use case)                            |
| **Interfaces Provided** | RecurrenceRule CRUD, next occurrence calculation, recurrence rule evaluation |
| **Dependencies**        | _(none)_                                                                     |

**Rationale:** Extracted as a shared sub-system because recurrence logic is used by both Subscriptions (renewal schedules) and Payments (recurring payments). Centralizing ensures consistent date calculation logic and avoids duplication.

---

### SS-09: Dashboard & Analytics

| Property                | Value                                                                                                    |
| ----------------------- | -------------------------------------------------------------------------------------------------------- |
| **Responsibility**      | Data aggregation, financial reports, payment calendar, analytics, report export                          |
| **Owned Entities**      | _(none — read-only aggregation)_                                                                         |
| **Use Cases**           | UC-07 View Dashboard, View Financial Reports, View Payment Calendar, View Asset Analytics, Export Report |
| **Interfaces Provided** | Dashboard data, reports (PDF/CSV), calendar data, analytics data                                         |
| **Dependencies**        | SS-01 Auth, SS-03 Asset (read), SS-04 Payment (read), SS-10 Subscription (read), SS-05 Document (read)   |

**Rationale:** Pure read-only sub-system that aggregates data from all other business sub-systems. Owns no entities — it queries across sub-systems to produce summaries, reports, and visualizations. Keeping it separate avoids polluting business sub-systems with reporting logic.

---

### SS-10: Subscription Management

| Property                | Value                                                                                                                                   |
| ----------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| **Responsibility**      | Subscription registration, renewal tracking, status management                                                                          |
| **Owned Entities**      | **Subscription**                                                                                                                        |
| **Use Cases**           | UC-04 Register Subscription, View Subscription Details, View Renewal Schedule, Update Subscription Status                               |
| **Interfaces Provided** | Subscription CRUD, subscription listing/filtering, status transitions, upcoming renewals query                                          |
| **Dependencies**        | SS-01 Auth, SS-03 Asset (optional link), SS-06 Classification (tags), SS-05 Document (attachments), SS-08 Scheduling (recurrence rules) |

**Rationale:** Manages subscription lifecycle independently from payments. While subscriptions generate payments, the sub-system itself focuses on the subscription entity and its renewal tracking. Payment generation can be triggered via events or direct service calls.

---

### SS-11: Administration

| Property                | Value                                                                                                                       |
| ----------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| **Responsibility**      | Admin user management, system statistics, audit logging, system configuration                                               |
| **Owned Entities**      | **AuditLog**                                                                                                                |
| **Use Cases**           | UC-08 Suspend User, View User Accounts, Reactivate User, View System Statistics, View Audit Logs, Configure System Settings |
| **Interfaces Provided** | Admin dashboard, user account management, audit trail, system configuration                                                 |
| **Dependencies**        | SS-01 Auth (admin authorization), SS-02 User Management (account status changes)                                            |

**Rationale:** Completely separate from end-user sub-systems. Admins have a different UI, different authorization rules, and must never access user data (FR-804). The audit log is owned here because it records administrative actions.

---

## Entity Ownership Map

| Entity                 | Owner Sub-System              | Consumed By                         |
| ---------------------- | ----------------------------- | ----------------------------------- |
| User                   | SS-02 User Management         | SS-01, SS-11, all others (via auth) |
| Asset                  | SS-03 Asset Management        | SS-04, SS-05, SS-09, SS-10          |
| Payment                | SS-04 Payment Management      | SS-03, SS-05, SS-07, SS-09          |
| Subscription           | SS-10 Subscription Management | SS-04, SS-05, SS-07, SS-09          |
| Document               | SS-05 Document Management     | SS-03, SS-04, SS-07, SS-10          |
| Category               | SS-06 Classification          | SS-03, SS-04                        |
| Tag                    | SS-06 Classification          | SS-03, SS-04, SS-10                 |
| RecurrenceRule         | SS-08 Scheduling              | SS-04, SS-10                        |
| Notification           | SS-07 Notification            | —                                   |
| NotificationPreference | SS-07 Notification            | SS-02 (creates defaults)            |
| AuditLog               | SS-11 Administration          | —                                   |

---

## Cross-Cutting Concerns

These are **not sub-systems** — they are aspects that cut across multiple sub-systems and will be implemented as shared infrastructure.

| Concern                    | Description                                                              | Affected Sub-Systems                                      |
| -------------------------- | ------------------------------------------------------------------------ | --------------------------------------------------------- |
| **Security**               | JWT filter chain, role-based authorization, data isolation enforcement   | All                                                       |
| **Soft Delete**            | `deleted_at` timestamp handling, query filtering for non-deleted records | All (except AuditLog)                                     |
| **Multi-Currency**         | Money value object handling, currency code validation                    | SS-03, SS-04, SS-10                                       |
| **Audit Logging**          | Recording administrative actions                                         | SS-11 (primary), potentially all for user-action auditing |
| **Pagination & Filtering** | Consistent paginated list responses with filtering/sorting               | All sub-systems with list endpoints                       |
| **Error Handling**         | Standardized error response format                                       | All                                                       |
| **Validation**             | Input validation framework                                               | All                                                       |

---

## Dependency Graph

```
SS-01 Auth <-------- (all sub-systems depend on Auth)
  |
  v
SS-02 User Mgmt <-- SS-11 Admin
  |
  v
SS-08 Scheduling (shared, no deps)
  |
  +----> SS-10 Subscription ----+
  |         |                    |
  +----> SS-04 Payment ---------+-----> SS-09 Dashboard
  |         |                    |         (read-only)
  |         v                    |
  +----> SS-03 Asset -----------+
  |         |
  v         v
SS-06 Classification (shared)

SS-05 Document <--- SS-03, SS-04, SS-10

SS-07 Notification --reads--> SS-04, SS-10, SS-05
  |
  v
Email Service (external)
```

---

## Sub-System to Java Package Mapping (Preview)

This will be formalized in Step 8 (Package Diagram), but a preview:

```
com.trackwise
├── auth/           # SS-01
├── user/           # SS-02
├── asset/          # SS-03
├── payment/        # SS-04
├── document/       # SS-05
├── classification/ # SS-06
├── notification/   # SS-07
├── scheduling/     # SS-08
├── dashboard/      # SS-09
├── subscription/   # SS-10
├── admin/          # SS-11
└── common/         # Cross-cutting concerns
    ├── security/
    ├── money/
    ├── pagination/
    ├── exception/
    └── audit/
```
