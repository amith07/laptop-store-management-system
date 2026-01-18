# Laptop Store Management System

## Overview
Laptop Store Management System is a Spring Boot RESTful application designed to manage an online laptop store with role-based access control. The system supports brand and laptop management, cart operations, order lifecycle handling, and payment initiation.

This project is structured and implemented following enterprise-grade best practices suitable for capstone projects and interviews.

---

## Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- MySQL
- Maven
- Postman (for API testing)

---

## Roles & Access Control

| Role | Description |
|-----|------------|
| CUSTOMER | Browse laptops, manage cart, place & cancel orders |
| MANAGER | Manage order processing (view pending orders, lifecycle actions) |
| ADMIN | Full access including manager-level endpoints |

> Admin access to manager endpoints is explicitly allowed using `hasAnyRole('MANAGER','ADMIN')`.

---

## Implemented Modules

### 1. Brand Module
- Create brand (ADMIN)
- List brands (PUBLIC)
- Delete brand (ADMIN)

### 2. Laptop Module
- Add / update / delete laptops (ADMIN)
- Public laptop search & listing
- Stock management during checkout

### 3. Cart Module
- Add laptop to cart (CUSTOMER)
- Update quantity
- Remove item
- View cart

### 4. Order Module (Lifecycle Implemented)
- Checkout cart → creates order
- Order statuses:
  - CREATED
  - COMPLETED
  - CANCELLED
- Cancel order (only valid for COMPLETED orders)
- Restore stock on cancellation
- View order history (CUSTOMER)

### 5. Payment Module
- Payment initiation endpoint
- GET methods intentionally not exposed for payments
- Payment confirmation to be added next

---

## Order Lifecycle Rules
- Cart checkout creates an order
- Stock is reduced at checkout
- Only COMPLETED orders can be cancelled
- Cancelling restores laptop stock
- Invalid transitions are blocked via business rules

---

## Security Highlights
- HTTP Basic Authentication (current)
- Role-based endpoint protection using `@PreAuthorize`
- Explicit access control (no implicit role hierarchy)
- Clear separation between public and secured APIs

---

## API Testing (Postman)

### Customer Flow
1. GET `/api/brands`
2. GET `/api/laptops`
3. POST `/api/cart/add`
4. POST `/api/orders/checkout`
5. GET `/api/orders`
6. POST `/api/orders/{id}/cancel`

### Manager / Admin Flow
1. GET `/api/manager/orders`
2. Process lifecycle actions (next phase)

---

## Current Status
✔ Brand Management  
✔ Laptop Management  
✔ Cart Management  
✔ Order Lifecycle (Create / Cancel)  
✔ Role-based Security  
✔ Global Exception Handling  

---

## Next Planned Enhancements
- Payment confirmation & verification
- Order shipping & delivery lifecycle
- Mapper layer refactor (`com.ey.mapper`)
- Unit & integration tests
- JWT authentication (optional)
- Admin analytics endpoints

---

## Notes
- GET requests are intentionally not allowed for payment actions
- All business validations are enforced at service layer
- Designed to be extensible and interview-ready

---

## Author
Capstone Project – Laptop Store Management System
