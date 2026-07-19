# UC-07: View Dashboard

| Field            | Value                  |
| ---------------- | ---------------------- |
| **Use Case ID**  | UC-07                  |
| **Name**         | View Dashboard         |
| **Actor**        | End User               |
| **Priority**     | High                   |
| **FR Reference** | FR-601, FR-602, FR-603 |

## Description

An authenticated user views their personalized dashboard, which aggregates summary statistics, upcoming obligations, and recent activity across all asset management areas.

## Preconditions

- The user is authenticated.

## Main Flow

1. The user navigates to the dashboard (default landing page after login).
2. The system retrieves aggregated data for the authenticated user.
3. The system displays the dashboard with:
   - **Summary cards:** Total assets (by status), active subscriptions count, pending payments count, documents count.
   - **Upcoming payments:** List of next N payments due, sorted by due date.
   - **Upcoming renewals:** List of next N subscription renewals.
   - **Expiring documents:** Documents with expiration dates approaching.
   - **Recent activity:** Last N actions performed by the user.
   - **Monthly spending summary:** Total spending for the current month vs. previous month.
4. The user can click on any item to navigate to its detail page.

## Alternative Flows

### AF-1: Empty Dashboard (New User)

- **At step 3:** The user has no assets, subscriptions, or payments.
- The system displays a welcome message with guided actions: "Get started by registering your first asset."
- Quick-action buttons are displayed for Register Asset, Register Subscription, Register Payment.

### AF-2: Filter by Date Range

- **At step 3:** The user selects a custom date range for the spending summary.
- The system recalculates and displays the data for the selected period.

## Postconditions

- The dashboard is displayed with current aggregated data.
- No data is modified.
