# Laptop Store Management System – REST Backend

## Project Overview

Laptop Store Management System is a **Spring Boot 3.x REST-only backend** built as a capstone-level e-commerce application. The project demonstrates **clean layered architecture**, **stateless JWT-based security**, **global logging with correlation IDs**, and **realistic business workflows** for an online laptop store.

Development follows an incremental, production-first approach, with each module stabilized before moving forward.

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
* SLF4J + Logback

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
logging     → request tracing & observability
exception   → centralized error handling
config      → application configuration
```

Key rules:

* No business logic in controllers
* No entity exposure in APIs
* Centralized exception handling
* Role enforcement handled at the HTTP security layer
* Logging is cross-cutting and non-invasive

---

## Security (FINALIZED)

### Authentication

* **JWT-based stateless authentication**
* Login endpoint:

```
POST /auth/login
```

* Issues signed JWT access tokens
* Tokens sent via:

```
Authorization: Bearer <JWT>
```

* JWT validated on every request using a custom filter
* SecurityContext populated from JWT claims

HTTP Basic authentication and server-side sessions are **fully removed**.

---

### Authorization

* Role-based access enforced **centrally in SecurityConfig (HTTP layer)**
* Method-level role annotations intentionally avoided for JWT stability

Supported authorities:

* `ROLE_ADMIN`
* `ROLE_MANAGER`
* `ROLE_CUSTOMER`

Roles are embedded directly in JWT claims and mapped to Spring Security authorities.

---

## Global Logging & Observability

### Features

* Request/response logging for every API call
* Unique `requestId` generated per request
* Authenticated `username` captured from SecurityContext
* MDC (Mapped Diagnostic Context) used for log correlation
* Entry, exit, and business-decision logs

### Example Log Output

```
INFO  [c9b1a2e4] [customer] Incoming request: POST /api/orders
INFO  [c9b1a2e4] [customer] Starting checkout for user=customer
INFO  [c9b1a2e4] [customer] Order 15 successfully created for user=customer
INFO  [c9b1a2e4] [customer] Outgoing response: POST /api/orders (status=201)
```

This enables production-grade debugging and traceability.

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
* Cart ownership derived from JWT SecurityContext

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
GET /api/admin/orders                     (ROLE_ADMIN)
GET /api/manager/orders/status/{status}  (ROLE_MANAGER / ROLE_ADMIN)
```

---

## Payment Module (Partial)

* Payment initiation endpoint exists
* POST-only enforcement
* Payment confirmation and refund logic pending

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
* Stateless JWT security
* Centralized role enforcement
* Global logging with MDC
* Brand, Laptop, Cart modules
* Order checkout, cancellation, history
* Inventory reconciliation
* Swagger integration

---

## What Is Pending

* Unit & integration testing (JUnit, Mockito)
* Extended order lifecycle (PROCESSING, SHIPPED, DELIVERED, REFUNDED)
* Payment confirmation & refund flow
* Pagination, sorting, filtering
* Audit & admin action logs

---

## Running the Application

1. Configure MySQL database
2. Update `application.yml`
3. Run the application

Base URL:

```
http://localhost:8080
```

---

## Project Status

**Status:** Active development
**Security:** JWT-based, stateless, production-ready
**Observability:** Global logging with request tracing enabled
**Next Milestone:** Unit & integration testing

---

Amith R - Capstone Project – Laptop Store Management System
