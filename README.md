# Laptop Store Management System – REST Backend

## Project Overview

Laptop Store Management System is a **Spring Boot 3.x REST-only backend** designed as a capstone-level e-commerce application. The project demonstrates **clean layered architecture**, **JWT-based stateless security**, and **realistic business workflows** for an online laptop store.

The system is intentionally built incrementally, with production-grade decisions at each stage.

---

## Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Security 6.x
* JWT (JSON Web Tokens)
* Spring Data JPA (Hibernate)
* MySQL
* Maven
* Swagger / OpenAPI

---

## Architecture

Strict layered architecture is enforced:

```
controller  → REST endpoints only
service     → business logic & validation
repository  → JPA persistence
model       → JPA entities
dto         → request / response payloads
security    → authentication & authorization
exception   → centralized error handling
config      → application configuration
```

Rules enforced:

* No business logic in controllers
* No entity exposure in APIs
* Centralized exception handling
* Role enforcement handled at security layer

---

## Security (Current – FINALIZED)

### Authentication

* **JWT-based authentication (stateless)**
* Login endpoint:

```
POST /auth/login
```

* Issues signed JWT access token
* Token passed via HTTP header:

```
Authorization: Bearer <JWT>
```

* JWT validated on every request via a custom filter
* SecurityContext populated from JWT claims

> HTTP Basic authentication has been **fully removed**.

---

### Authorization

* Role-based access enforced **at the SecurityConfig (HTTP layer)**
* Method-level role checks removed to avoid JWT + Spring Security 6 inconsistencies

Supported roles:

* `ROLE_ADMIN`
* `ROLE_MANAGER`
* `ROLE_CUSTOMER`

Roles are embedded directly in JWT claims and mapped to `GrantedAuthority` objects.

---

## User & Role Model

### User

* username (unique)
* email (unique)
* password (BCrypt encoded)
* status: `ACTIVE | LOCKED | DISABLED`

### Role

* ADMIN
* MANAGER
* CUSTOMER

Users and roles are mapped using a **many-to-many relationship**.

---

## Brand Module

### Features

* Create brand (ADMIN / MANAGER)
* Fetch brands (public)

### Endpoints

```
GET  /api/brands          (public)
POST /api/brands          (ADMIN / MANAGER)
```

---

## Laptop Module

### Features

* Create & update laptops (ADMIN / MANAGER)
* Fetch laptops (public)
* Search laptops (public)
* Stock management

### Endpoints

```
GET  /api/laptops
POST /api/laptops/search
POST /api/laptops         (ADMIN / MANAGER)
```

---

## Cart Module

### Features

* One active cart per customer
* Add, update, remove items
* Cart ownership derived from SecurityContext

### Endpoints

```
POST   /api/cart/items
PATCH  /api/cart/items/{laptopId}
DELETE /api/cart/items/{laptopId}
GET    /api/cart
```

**Access:** `ROLE_CUSTOMER`

---

## Order Module

### Order Lifecycle (Current)

```
CREATED → COMPLETED → CANCELLED
```

### Business Rules

* Orders created only from authenticated customer cart
* Cart must not be empty
* Stock validated and reduced during checkout
* Stock restored on cancellation
* Customers can access only their own orders

### Endpoints

```
POST /api/orders                (ROLE_CUSTOMER)
GET  /api/orders                (ROLE_CUSTOMER)
POST /api/orders/{id}/cancel    (ROLE_CUSTOMER)
```

### Admin / Manager Views

```
GET /api/admin/orders                 (ROLE_ADMIN)
GET /api/manager/orders/status/{status}  (ROLE_MANAGER / ROLE_ADMIN)
```

---

## Payment Module (Partial)

* Payment initiation endpoint exists
* POST-only enforcement
* Payment confirmation and refunds pending

---

## JWT Flow Summary

1. Client logs in via `/auth/login`
2. Receives JWT access token
3. Sends token on every secured request
4. JWT filter validates token and sets SecurityContext
5. Role enforcement handled at SecurityConfig level

---

## Exception Handling

* Centralized `GlobalExceptionHandler`
* Custom `ApiException`
* `ApiErrorCode` enum for consistent error mapping
* Proper HTTP status codes

---

## What Is Completed

* Clean layered architecture
* Database-backed authentication
* JWT login endpoint
* JWT validation filter
* Stateless security configuration
* Role-based access at HTTP layer
* Brand, Laptop, Cart modules
* Order checkout, cancellation, history
* Inventory reconciliation
* Swagger integration

---

## What Is Pending

* Global logging (SLF4J + MDC)
* Unit & integration testing
* Extended order lifecycle (PROCESSING, SHIPPED, DELIVERED, REFUNDED)
* Payment confirmation & refund flow
* Pagination, sorting, filtering
* Audit & admin action logs

---

## Running the Application

1. Configure MySQL database
2. Update `application.yml`
3. Run application

Base URL:

```
http://localhost:8080
```

---

## Project Status

**Status:** Active development
**Security:** JWT-based, stateless, production-ready
**Next Milestone:** Global logging & observability

---

Amith R - Capstone Project – Laptop Store Management System
