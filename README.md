# Laptop Store Management System (REST Backend)

## Project Overview

Laptop Store Management System is a **Spring Boot 3.x REST-only backend** designed as a capstone-level e-commerce system. The application follows **clean layered architecture**, enforces **role-based security**, and models realistic business flows for browsing laptops, managing carts, placing orders, and handling inventory.

The project is being developed **incrementally**, with each phase stabilised before moving to the next.

---

## Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Security (HTTP Basic – temporary)
* Spring Data JPA
* MySQL
* Hibernate
* Swagger / OpenAPI

---

## Architecture

Strict layered separation is enforced:

```
controller  → REST endpoints only
service     → business logic & validation
repository  → persistence layer (JPA)
model       → JPA entities
dto         → request / response payloads
security    → authentication & authorization
exception   → centralized error handling
config      → bootstrap & configuration
```

❌ No business logic in controllers
❌ No entity exposure in APIs
✅ Centralized exception handling

---

## Security (Current State)

### Authentication

* **HTTP Basic Authentication** (temporary)
* Users are **database-backed** (no in-memory users)
* Passwords stored using **BCrypt hashing**

### Roles

* `ADMIN`
* `MANAGER`
* `CUSTOMER`

Role checks enforced using `@PreAuthorize`.

> ⚠️ JWT / OAuth2 authentication is planned next and not yet implemented.

---

## User & Role Model

* `User`

  * username (unique)
  * email (unique)
  * password (BCrypt)
  * status: `ACTIVE | LOCKED | DISABLED`

* `Role`

  * ADMIN
  * MANAGER
  * CUSTOMER

Users and roles are linked via a **many-to-many relationship**.

Initial users are seeded at startup:

| Username | Password    | Role     |
| -------- | ----------- | -------- |
| admin    | admin123    | ADMIN    |
| manager  | manager123  | MANAGER  |
| customer | customer123 | CUSTOMER |

---

## Brand Module

### Features

* Create brand (ADMIN / MANAGER)
* Fetch all brands (public)

### Endpoints

```
GET  /api/brands        (public)
POST /api/brands        (ADMIN / MANAGER)
```

---

## Laptop Module

### Features

* Create & update laptops (ADMIN / MANAGER)
* Fetch laptops (public)
* Search laptops (public)
* Stock tracked per laptop

### Endpoints

```
GET  /api/laptops
POST /api/laptops/search
POST /api/laptops        (ADMIN / MANAGER)
```

---

## Cart Module

### Features

* One active cart per customer
* Add, update, remove items
* Cart tied to **authenticated user**
* No hardcoded usernames

### Endpoints

```
POST   /api/cart/items
PATCH  /api/cart/items/{laptopId}
DELETE /api/cart/items/{laptopId}
GET    /api/cart
```

**Role:** CUSTOMER only

---

## Order Module

### Order Lifecycle (Current)

```
CREATED → COMPLETED → CANCELLED
```

### Business Rules

* Orders are created from the customer cart
* Cart must not be empty
* Stock is validated and reduced on checkout
* Stock is restored on cancellation
* Customers can only access their own orders

### Endpoints

```
POST /api/orders              (CUSTOMER)  → checkout
GET  /api/orders              (CUSTOMER)  → order history
POST /api/orders/{id}/cancel  (CUSTOMER)
```

### Admin / Manager

```
GET /api/admin/orders                 (ADMIN)
GET /api/manager/orders?status=...    (MANAGER / ADMIN)
```

---

## Payment Module (Partial)

### Current State

* Payment initiation exists
* Payment confirmation & refunds not yet implemented
* Only POST methods allowed (no GET for payments)

> Payment lifecycle will be completed after JWT integration.

---

## Exception Handling

* Centralized `GlobalExceptionHandler`
* Custom `ApiException`
* Structured error responses using `ApiErrorCode`
* Correct HTTP status mapping

---

## What Is Completed

✅ Clean architecture established
✅ DB-backed authentication
✅ Role-based authorization
✅ Brand, Laptop, Cart modules
✅ Order checkout, cancellation & history
✅ Inventory reconciliation on orders
✅ Swagger enabled

---

## What Is Pending (Next Phases)

1. JWT Authentication & OAuth2 Resource Server
2. Refresh tokens & stateless security
3. Mapper layer (MapStruct or manual mappers)
4. Global logging with MDC
5. Unit & integration tests
6. Order lifecycle expansion (PROCESSING, SHIPPED, DELIVERED, REFUNDED)
7. Payment completion & refunds
8. Pagination & sorting

---

## How to Run

1. Configure MySQL database
2. Update `application.yml`
3. Run application

```
http://localhost:8080
```


---

## Project Status

**Status:** 🚧 In Progress (Security hardening phase complete)
**Next Milestone:** JWT Authentication

---

## Author

Amith R - Capstone Project – Laptop Store Management System
