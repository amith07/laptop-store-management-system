# Laptop Store Management System

## Overview

The **Laptop Store Management System** is a REST-only backend application built using **Spring Boot 3.x** and **Java 17**. It provides a role-based platform for managing laptop brands, products, carts, orders, and payments.

The system is designed with **clean layered architecture**, strong separation of concerns, and is fully testable via **Postman**.

---

## Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Data JPA
* Spring Security (HTTP Basic Auth)
* MySQL
* Maven

---

## Architecture

The application follows a strict layered architecture:

```
controller   -> REST endpoints only
service      -> Business logic & validation
repository   -> Data access (JPA)
model        -> JPA entities
dto          -> Request / Response payloads
security     -> Authentication & authorization
config       -> Application & security configuration
exception    -> Centralized error handling
```

**Rules enforced:**

* No business logic in controllers
* Validation handled in services
* No raw exceptions returned to clients

---

## Roles & Access Control

The system supports three roles:

| Role     | Description                         |
| -------- | ----------------------------------- |
| ADMIN    | Full system access                  |
| MANAGER  | Inventory and order management      |
| CUSTOMER | Shopping, cart, and order placement |

**Role hierarchy:**

```
ADMIN > MANAGER > CUSTOMER
```

Authentication is handled using **HTTP Basic Authentication**.

---

## Modules Implemented

### 1. Security

* Role-based access control
* Method-level security using annotations
* Public and protected endpoints explicitly defined

---

### 2. Brand Module

**Purpose:** Manage laptop brands

**Features:**

* Create brand (ADMIN / MANAGER)
* Get all brands (Public)
* Get brand by ID (Public)
* Delete brand (ADMIN)

---

### 3. Laptop Module

**Purpose:** Manage laptop inventory

**Features:**

* Add laptop (ADMIN / MANAGER)
* Update laptop (ADMIN / MANAGER)
* Delete laptop (ADMIN)
* Get all laptops (Public)
* Get laptop by ID (Public)
* Get laptops by brand (Public)

Laptop data includes price, stock quantity, brand association, and specifications.

---

### 4. Cart Module

**Purpose:** Customer shopping cart management

**Features:**

* Add laptop to cart (CUSTOMER)
* Update item quantity (CUSTOMER)
* Remove item from cart (CUSTOMER)
* View cart (CUSTOMER)

Each cart is associated with the authenticated customer.

---

### 5. Order Module

**Purpose:** Order processing and management

**Controller separation by role:**

* Customer Order Controller
* Manager Order Controller
* Admin Order Controller

**Features:**

* Place order from cart (CUSTOMER)
* View own orders (CUSTOMER)
* Update order status (MANAGER / ADMIN)
* View all orders (ADMIN)

Order lifecycle is managed through well-defined statuses.

---

### 6. Payment Module

**Purpose:** Handle order payments

**Features:**

* Make payment for an order (CUSTOMER)
* View payment details
* Track payment status

> Note: Payment logic is internal and does not integrate with external gateways (capstone scope).

---

## Exception Handling

* Centralized exception handling
* Custom business exceptions
* Meaningful HTTP status codes returned
* Consistent error response structure

---

## Database

* MySQL database
* JPA/Hibernate used for ORM
* Proper entity relationships defined

---

## Running the Application

### Prerequisites

* Java 17
* Maven
* MySQL

### Steps

1. Create a MySQL database
2. Update database credentials in `application.yml`
3. Build the project:

   ```bash
   mvn clean install
   ```
4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

The application starts on the configured port (default: 8080).

---

## Testing with Postman

* All endpoints are REST-based
* Use HTTP Basic Auth
* Set username/password according to role
* Refer to the provided API documentation for request bodies

---

## Project Status

* Core modules implemented and functional
* Role-based security enforced
* Ready for evaluation and further enhancements

---

## Future Enhancements (Optional)

* JWT-based authentication
* Payment gateway integration
* Product reviews & ratings
* Pagination and sorting
* Unit and integration tests

---

## Author

Amith R - Capstone Project – Laptop Store Management System
