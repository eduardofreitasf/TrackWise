# TrackWise

**TrackWise** is a comprehensive personal asset management system designed to help users organize, track, and manage their valuable assets, subscriptions, payments, and critical documents in one unified platform.

## Overview

Managing personal assets, subscriptions, and payments across multiple platforms often leads to scattered data, forgotten renewals, and missed deadlines. TrackWise aims to centralize the management of all personal financial commitments and assets into a single, intelligent, and organized platform.

Key features include:

- **Asset Management**: Register and track cars, properties, appliances, and other valuable assets with complete historical and future payment data.
- **Subscription Management**: Monitor recurring service subscriptions (Netflix, Spotify, etc.) with renewal tracking and financial analytics.
- **Payment Management**: Log and organize one-time and recurring payments, categorize expenses, and maintain a complete financial history.
- **Document Management**: Store, organize, and retrieve critical documents (registrations, insurance policies, receipts, warranties) with expiration tracking.
- **Smart Notifications**: Receive timely alerts for upcoming payments, subscription renewals, and document expirations based on your preferences.
- **Financial Dashboard & Analytics**: View comprehensive reports on spending patterns, asset costs, payment history, and financial trends over time.
- **Multi-User System**: Secure, isolated user accounts with complete data separation and independent asset management.
- **Administrative Backoffice**: System administrators can manage user accounts and view system statistics without accessing individual user data.

## Documentation

- **Requirements:** The comprehensive Project Proposition and Requirements Document can be found in [`proposal.pdf`](docs/proposal/roposal.pdf).
- **System Modeling:** Future system modeling artifacts (such as UML Class Diagrams, Entity-Relationship Diagrams, and API specifications) will be stored in the [`docs/`](docs/) directory prior to the implementation phase.

## Technology Stack (Planned)

- **Backend:** Java with Spring Boot framework
- **Database:** PostgreSQL or MySQL (relational database with proper indexing and partitioning)
- **Authentication:** JWT-based token authentication with bcrypt password hashing
- **File Storage:** Cloud-based or on-premise file storage with encryption and access controls
- **Notification System:** Scheduled background jobs for notification evaluation; email service for delivery
- **Frontend:** Responsive web application aligned to TrackWise brand identity
- *(Additional tech stack details to be determined during implementation)*
