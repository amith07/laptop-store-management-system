# Laptop Store Management System (REST Backend)

## Project Overview

Laptop Store Management System is a **Spring Boot 3.x REST-only backend** developed as a capstone-level e-commerce system. The application demonstrates **clean architecture**, **modern security practices**, and **realistic business workflows** for an online laptop store.

The project is implemented **incrementally**, with each major feature stabilized before advancing to the next.

---

## Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Security 6.x
* JWT (JSON Web Tokens)
* Spring Data JPA (Hibernate)
* MySQL
* Swagger / OpenAPI
* Maven

---

## Architecture

The application strictly follows a layered architecture:

```
controller  → REST endpoints only
service     → business logic & validations
repository  → JPA persistence layer
model       → JPA entities
dto         → request / response payloads
security    → authentication & authorization
exception   → centralized error handling
config      → configuration & bootstrap
```

✔ No business logic in controllers
✔ No entity exposure in APIs
✔ Centralized exception handling

---

## Security (Current State)

### Authentication

* **JWT-based authentication** implemented
* `POST /auth/login` issues JWT access tokens
* Tokens are passed via:

```
Authorization: Bearer <token>
```

* JWT validated on every request via a custom filter
* SecurityContext populated from token claims

> ⚠️ HTTP Basic authentication is still temporarily enabled for backward compatibility. It will be removed in the next step when the system is made fully stateless.

---

### Authorization

Role-based access enforced using `@PreAuthorize`.

Supported roles:

* `ADMIN`
* `MANAGER`
* `CUSTOMER`

Role information is embedded in JWT claims and applied consistently across controllers.

---

## User & Role Model

* **User**

  * username (unique)
  * email (unique)
  * password (BCrypt encoded)
  * status: `ACTIVE | LOCKED | DISABLED`

* **Role**

  * ADMIN
  * MANAGER
  * CUSTOMER

* Users and roles are mapped using a **many-to-many relationship**

### Seeded Users (Dev/Test)

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
* Add, update, remove cart items
* Cart ownership derived from authenticated user (SecurityContext)
* No hardcoded usernames

### Endpoints

```
POST   /api/cart/items
PATCH  /api/cart/items/{laptopId}
DELETE /api/cart/items/{laptopId}
GET    /api/cart
```

**Access:** CUSTOMER

---

## Order Module

### Order Lifecycle (Current)

```
CREATED → COMPLETED → CANCELLED
```

### Business Rules

* Orders created from the authenticated user’s cart
* Cart must not be empty
* Stock validated and reduced during checkout
* Stock restored on cancellation
* Customers can access only their own orders

### Endpoints

```
POST /api/orders              (CUSTOMER)  → checkout
GET  /api/orders              (CUSTOMER)  → order history
POST /api/orders/{id}/cancel  (CUSTOMER)
```

### Admin / Manager Views

```
GET /api/admin/orders                 (ADMIN)
GET /api/manager/orders?status=...    (MANAGER / ADMIN)
```

---

## Payment Module (Partial)

### Current State

* Payment initiation endpoint exists
* Only POST methods allowed
* Payment lifecycle and refunds not yet implemented

---

## JWT Flow Summary

1. Client logs in:

```
POST /auth/login
```

2. Receives JWT access token
3. Sends token with each secured request
4. JWT filter validates token and sets SecurityContext

---

## Exception Handling

* Centralized `GlobalExceptionHandler`
* Custom `ApiException`
* Consistent error responses via `ApiErrorCode`
* Proper HTTP status mapping

---

## What Is Completed

* Clean layered architecture
* Database-backed authentication
* JWT issuance (`/auth/login`)
* JWT validation filter
* Role-based authorization
* Brand, Laptop, Cart modules
* Order checkout, cancellation, and history
* Inventory reconciliation
* Swagger integration

---

## What Is Pending (Next Phases)

1. Remove HTTP Basic & enable stateless session management
2. Refresh token support
3. Mapper layer completion
4. Global logging with MDC
5. Unit & integration tests
6. Extended order lifecycle (PROCESSING, SHIPPED, DELIVERED, REFUNDED)
7. Payment confirmation & refund flow
8. Pagination, sorting, and filtering

---

## Running the Application

1. Configure MySQL database
2. Update `application.yml`
3. Run the application

```
http://localhost:8080
```


---

## Project Status

**Status:** Active development
**Current Milestone:** JWT-based authentication enforced via filter
**Next Milestone:** Fully stateless security configuration

---

Capstone Project – Laptop Store Management System
