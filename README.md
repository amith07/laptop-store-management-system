# Laptop Store Management System

A Spring Boot–based backend application for managing an online laptop store.  
The system supports secure authentication, role-based authorization, cart management, order lifecycle handling, and payment processing.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot 3
- Spring Security (JWT Authentication)
- Spring Data JPA (Hibernate)
- MySQL 8
- Maven
- JUnit 5 & Mockito
- Swagger / OpenAPI

---

## 🔐 Authentication & Authorization

- Authentication is JWT-based
- Authorization is role-based
- JWT contains:
  - sub → username
  - roles → list of granted roles

### Supported Roles
- CUSTOMER
- ADMIN
- MANAGER

---

## 🛒 Cart Module

Endpoints:
- GET /api/cart
- POST /api/cart/items
- PATCH /api/cart/items/{laptopId}
- DELETE /api/cart/items/{laptopId}

Rules:
- One active cart per user
- Stock not reduced at add-to-cart
- Cart locked after checkout

---

## 📦 Order Module

Lifecycle:
CREATED → COMPLETED  
CREATED → CANCELLED

Endpoints:
- POST /api/orders
- GET /api/orders
- POST /api/orders/{orderId}/cancel
- GET /api/admin/orders
- GET /api/manager/orders/status/{status}

Rules:
- Checkout creates CREATED order
- Stock reduced on checkout
- Cancellation allowed only before payment

---

## 💳 Payment Module

Endpoint:
- POST /api/payments/orders/{orderId}

Rules:
- Only CREATED orders can be paid
- One payment per order
- Payment marks order COMPLETED

---

## 🧪 Testing

- Controller tests
- Service unit tests
- Repository tests
- Separate test schema

Run:
mvn test

---

## 📖 Swagger

http://localhost:8080/swagger-ui.html

---

## 📌 Status

All modules complete  
All tests passing  
JWT security enforced  
Ready for submission
