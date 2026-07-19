# UC-09: Process Notification Triggers (System)

| Field            | Value                           |
| ---------------- | ------------------------------- |
| **Use Case ID**  | UC-09                           |
| **Name**         | Process Notification Triggers   |
| **Actor**        | Notification Scheduler (system) |
| **Priority**     | High                            |
| **FR Reference** | FR-501, FR-502, FR-503, FR-504  |

## Description

A scheduled system job evaluates all notification triggers (upcoming payments, subscription renewals, document expirations, maintenance reminders) and creates notifications for users based on their configured preferences.

## Preconditions

- The scheduler is running and triggers at the configured interval.
- Users have notification preferences configured (defaults are created at registration).

## Main Flow

1. The scheduler triggers the notification evaluation job.
2. The system queries all upcoming events that fall within notification windows:
   - **Payments:** Due dates within the user's configured lead time (e.g., 3 days from now).
   - **Subscription renewals:** Next renewal dates within the user's configured lead time.
   - **Document expirations:** Expiration dates within the user's configured lead time.
3. For each triggerable event:
   a. The system checks the user's notification preferences for the event type.
   b. If the event type is disabled, skip.
   c. The system checks if a notification for this event has already been created (avoid duplicates).
   d. If no duplicate exists, the system creates a notification record with:
   - Type, title, message, trigger date, reference type and ID, channel.
4. For each notification with channel IN_APP or BOTH:
   - The notification is available immediately in the user's notification center.
5. For each notification with channel EMAIL or BOTH:
   - The system queues an email notification for delivery. _(extends: Send Email Notification)_
6. The job completes and logs the summary: total events evaluated, notifications created, emails queued.

## Alternative Flows

### AF-1: No Triggerable Events

- **At step 2:** No events fall within any user's notification windows.
- The job completes immediately with zero notifications created.

### AF-2: Email Service Unavailable

- **At step 5:** The email service is unavailable.
- The system marks the email notification as PENDING_RETRY.
- The next scheduler run will retry sending pending emails.
- The in-app notification is still created and available.

### AF-3: User Has Notifications Disabled

- **At step 3b:** The user has disabled the specific notification type.
- The event is skipped entirely for that user.

## Postconditions

- New notification records are created for all qualifying events.
- Email notifications are queued for delivery (if applicable).
- No duplicate notifications are created for the same event.
- The job execution is logged for monitoring purposes.
