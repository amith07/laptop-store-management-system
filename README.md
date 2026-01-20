# Laptop Store Management System

A Spring Boot–based RESTful backend for managing an online laptop store.
The system supports product catalog management, cart and order processing, payments, and secure role-based access using JWT authentication.

---

## 🛠 Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Security (JWT, stateless)
* Spring Data JPA (Hibernate)
* MySQL
* Maven
* Swagger / OpenAPI

---

## 🔐 Authentication & Authorization

* Stateless JWT authentication
* Login endpoint issues JWT tokens
* Role-based access control using authorities

### Roles

* CUSTOMER
* MANAGER
* ADMIN

---

## 👤 User Management

### Public

* Register new customer accounts

### Authenticated Users

* View own profile
* Update profile
* Change password

### Admin

* View users
* Update user role
* Activate / deactivate users

---

## 🏷️ Brand Management

* Update brand details
* Soft delete and restore brands
* Public brand listing supported

Access controlled for ADMIN / MANAGER.

---

## 💻 Laptop Management

* Update laptop details and stock
* Soft delete and restore laptops
* Search laptops (public)

Stock level automatically updates laptop availability status.

---

## 🛒 Cart Management (CUSTOMER)

* Add items to cart
* Update item quantity
* Remove items
* View active cart

Only one active cart per customer is allowed.

---

## 📦 Order Management

### Customer

* Place order (checkout)
* Cancel eligible orders
* View orders by status

### Manager

* View pending orders
* View today’s orders

---

## 💳 Payment Management

### Customer

* Pay for orders
* Request refunds
* View own payment history

### Admin

* View all payments
* Filter payments by status

Payments support:

* PENDING
* SUCCESS
* REFUNDED
* FAILED

---

## 📡 API Endpoints (Current Implementation)

### Authentication

* POST /auth/login

### Users

* POST /users/register
* GET /users/me
* PUT /users/me
* PUT /users/me/password

### Admin – Users

* GET /api/admin/users/{id}
* PUT /api/admin/users/{id}/role
* PUT /api/admin/users/{id}/status

### Brands

* PUT /api/brands/{id}
* DELETE /api/brands/{id}
* POST /api/brands/{id}/restore

### Laptops

* PUT /api/laptops/{id}
* PATCH /api/laptops/{id}/stock
* DELETE /api/laptops/{id}
* POST /api/laptops/{id}/restore
* POST /api/laptops/search

### Cart (CUSTOMER)

* POST /api/cart/items
* PATCH /api/cart/items/{laptopId}
* DELETE /api/cart/items/{laptopId}
* GET /api/cart/view

### Orders

* POST /api/orders/{orderId}/cancel
* GET /api/orders/status/{status}
* GET /api/manager/orders/pending
* GET /api/manager/orders/today

### Payments

* POST /api/payments/orders/{orderId}
* POST /api/payments/{paymentId}/refund
* GET /api/payments
* GET /api/payments/status/{status}
* GET /api/payments/admin

### Utility

* GET /ping

---

## 🗄️ Database Notes

* MySQL database
* Soft delete implemented for brands and laptops
* DataInitializer seeds roles and default users
* JPA lifecycle hooks used for auditing and pricing logic

---

## 🚀 Running the Application

1. Configure MySQL in application.properties
2. Set JWT secret and expiration
3. Run:
   mvn spring-boot:run
4. Access APIs at:
   [http://localhost:8080](http://localhost:8080)

---

## ✅ Project Status

* Core business flows implemented
* JWT security stable
* Role-based access enforced
* Ready for submission / evaluation
