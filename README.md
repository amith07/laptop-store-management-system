# Laptop Store Management System

## Project Overview

The **Laptop Store Management System** is a production-grade RESTful backend built using **Spring Boot 3.x** and **Java 17**. It models a real-world e-commerce system with secure authentication, role-based authorization, clean architecture, and comprehensive automated testing.

This project is designed as a **capstone-level backend application**, suitable for evaluation, extension, or real-world adaptation.

---

## Tech Stack

* **Java 17**
* **Spring Boot 3.5.x**
* **Spring Security (JWT + Method-Level Security)**
* **Spring Data JPA (Hibernate)**
* **MySQL** (separate schemas for runtime and tests)
* **JUnit 5 / Mockito / MockMvc**
* **Springdoc OpenAPI (Swagger)**
* **SLF4J + Logback (centralized logging)**

---

## Architecture

The application strictly follows a layered architecture:

```
controller  ->  service  ->  repository  ->  database
               |
               -> validation / business rules
```

Packages:

* `controller` – REST endpoints (no business logic)
* `service` – core business logic & validation
* `repository` – JPA repositories
* `model` – JPA entities
* `dto` – request / response models
* `security` – JWT, filters, configs
* `exception` – centralized error handling
* `logging` – request/response tracing

---

## Security Model

### Authentication

* **JWT-based authentication**
* Stateless backend
* Token issued via login endpoint
* JWT contains:

  * `sub` (username)
  * `roles` (ROLE_CUSTOMER / ROLE_MANAGER / ROLE_ADMIN)

### Authorization

* Enforced using `@PreAuthorize`
* Role hierarchy:

```
ADMIN > MANAGER > CUSTOMER
```

---

## Roles & Access Rules

### CUSTOMER

* Manage cart
* Place orders
* View own orders
* Cancel own orders (business-rule restricted)

### MANAGER

* View orders by status

### ADMIN

* View all orders
* Full visibility across system

---

## Core Features Implemented

### Cart Module

* Add item to cart
* Update quantity
* Remove item
* View cart

### Order Module

* Checkout cart → create order
* Order lifecycle enforcement
* Cancel order with stock restoration
* Order history

### Inventory Management

* Stock reduction on checkout
* Stock restoration on cancellation
* Insufficient stock validation

---

## Testing Strategy (COMPLETED)

### Unit Tests

* **Service layer**

  * Business rules
  * Validation failures
  * Exception scenarios

### Repository Tests

* Real MySQL schema (test profile)
* Query correctness
* Sorting & filtering validation

### Controller & Security Tests

* `MockMvc` based
* Role-based access verification
* 200 / 403 / 201 status validation
* End-to-end security enforcement

> ✅ **21 tests, 0 failures**

---

## Logging & Observability

* Centralized request/response logging
* Correlation ID per request
* User context logging
* Business-level logs in services

---

## How to Run

### Prerequisites

* Java 17
* MySQL 8.x

### Steps

1. Create database schemas:

   * `laptop_store`
   * `laptop_store_test`
2. Update credentials in `application.yml`
3. Run application:

```bash
mvn spring-boot:run
```

### Run Tests

```bash
mvn test
```

---

## Current Status

✔ Security (JWT)
✔ Cart & Order modules
✔ Inventory consistency
✔ Logging
✔ Repository, Service, Controller tests
✔ Clean build

---

## Next Planned Enhancements

* Global exception response standardization
* Payment confirmation & refund flow
* Pagination & sorting
* Audit fields (createdBy / updatedBy)
* Dockerization

---

## Evaluation Readiness

This project is:

* **Fully runnable**
* **Secure**
* **Test-covered**
* **Production-aligned**

Ready for capstone evaluation or further extension.
