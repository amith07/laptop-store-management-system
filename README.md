# Laptop Store Management System

A Spring Boot–based backend application for managing an online laptop store.  
The system supports cart management, order lifecycle handling, secure payments, refunds, and role-based access using JWT authentication.

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- MySQL
- Maven

---

## Core Features

### Authentication & Authorization
- JWT-based authentication
- Role-based access control
  - CUSTOMER
  - MANAGER
  - ADMIN
- Secure endpoints using method-level security

---

## Cart Management
- Add items to cart
- Update quantities
- Calculate total amount
- One active cart per user
- Cart is locked after checkout

---

## Order Lifecycle

Order statuses:
- CREATED
- COMPLETED
- CANCELLED

Flow:
1. Customer adds items to cart
2. Checkout creates an order with status `CREATED`
3. Payment completes the order
4. Order can be cancelled only via refund flow

---

## Payment Lifecycle

Payment statuses:
- PENDING
- SUCCESS
- REFUNDED

Flow:
1. Payment allowed only for `CREATED` orders
2. Successful payment:
   - Creates a payment record
   - Updates order status to `COMPLETED`
   - Deducts stock
3. Refund:
   - Allowed only for `SUCCESS` payments
   - Updates payment to `REFUNDED`
   - Cancels the order
   - Restores stock

---

## API Endpoints (Key)

### Cart
- `POST /api/cart/items` – Add item to cart

### Orders
- `POST /api/orders` – Checkout cart
- `GET /api/orders` – View order history (customer)
- `GET /api/admin/orders` – View all orders (admin)
- `GET /api/manager/orders?status=` – Filter by status (manager)

### Payments
- `POST /api/payments/orders/{orderId}` – Pay for an order
- `POST /api/payments/{paymentId}/refund` – Refund a payment

---

## Security Notes
- All protected endpoints require a valid JWT
- Users can only access their own orders and payments
- Unauthorized access returns 404 to prevent data leakage

---

## Database Notes
- MySQL is used for both development and testing
- Separate schema recommended for tests
- Enum values are stored as STRING to avoid ordinal issues

---

## Current Project State

- Cart, Order, Payment, and Refund flows fully implemented
- Business rules enforced at service layer
- Logging added for all major flows
- Integration-tested manually using Postman

---

## Future Enhancements
- Payment history endpoint
- Admin-managed refunds
- Shipment lifecycle
- Idempotent payment handling
- Automated integration tests
