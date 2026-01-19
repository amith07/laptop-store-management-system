# Laptop Store Management System

## Overview
Laptop Store Management System is a Spring Boot–based backend application that provides a complete e-commerce workflow for purchasing laptops. It supports role-based access, cart management, order processing, payments, and refunds using JWT-based authentication and MySQL as the primary database.

---

## Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- MySQL 8
- Maven
- JUnit & Mockito
- Postman (API testing)

---

## Core Features Implemented

### Authentication & Authorization
- JWT-based authentication
- Role-based access control:
  - CUSTOMER
  - ADMIN
  - MANAGER

---

### Cart Management
- Add items to cart
- View cart (customer-only)
- Prevent checkout with empty cart

---

### Order Management
- Checkout cart → creates order with status CREATED
- Order lifecycle:
  - CREATED
  - COMPLETED
  - CANCELLED
- Cancel order (only before payment)
- Restore stock on order cancellation
- View:
  - Customer order history
  - Admin all orders
  - Manager orders by status

---

### Payment Management (Latest)
- Pay for an order (customer-only)
- One payment per order enforced
- Payment lifecycle:
  - PENDING
  - SUCCESS
  - REFUNDED
- Payment automatically completes order
- Refund payment:
  - Valid only for SUCCESS payments
  - Updates payment status to REFUNDED
  - Cancels the order
  - Restores laptop stock

---


## API Endpoints Summary

### Cart
- POST /api/cart/items
- GET /api/cart

### Orders
- POST /api/orders
- POST /api/orders/{orderId}/cancel
- GET /api/orders
- GET /api/admin/orders
- GET /api/manager/orders/status/{status}

### Payments
- POST /api/payments/orders/{orderId}
- POST /api/payments/{paymentId}/refund

---

## Testing
- All unit and integration tests passing
- Controller, service, and repository layers covered
- MySQL test schema isolated via test profile

---

## Current Status
Stable build
Payments & refunds working correctly
Database schema aligned with enums
Ready for enhancements

---

## Next Possible Enhancements
- Payment history API
- Admin override refunds
- Refund time window restrictions
- Idempotency protection for payments
- Integration tests for payment & refund flows

---

## Author
Amith R - Capstone Project – Laptop Store Management System
