# Laptop Store Management System

A Spring Boot–based RESTful backend application for managing a laptop e-commerce platform.  
The system supports product browsing, cart management, order placement, and payment initiation with role-based security.

---

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- MapStruct
- Log4j2
- Maven

---

## Project Structure

```
com.ey
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── enums
├── exception
├── mapper
├── model
├── repository
├── security
└── service
```

---

## Roles & Access Control

| Role      | Permissions |
|-----------|------------|
| ADMIN     | Manage brands and laptops |
| CUSTOMER  | Cart, orders, checkout, payments |
| PUBLIC    | View laptops and search |

Authentication currently uses **Basic Authentication (in-memory users)** for development.

---

## Modules Implemented

### 1. Brand Module
- Add brand (ADMIN)
- View all brands (PUBLIC)
- Duplicate brand name validation

### 2. Laptop Module
- Add / update / delete laptops (ADMIN)
- Public laptop listing
- Advanced search:
  - Brand
  - Price range
  - CPU
  - Availability status
- Stock and price management

### 3. Cart Module
- Add laptop to cart
- Update cart item quantity
- Remove item from cart
- View current cart
- Validations:
  - Empty cart
  - Stock availability

### 4. Order Module
- Checkout cart to create order
- View customer orders
- Cancel order (only if not completed)
- Order items store price snapshot at purchase time

### 5. Payment Module
- Initiate payment for an order
- Payment endpoint is **POST-only**
- GET requests are intentionally not supported

---

## API Endpoints

### Public Endpoints
```
GET /api/laptops
GET /api/laptops/search
GET /api/brands
```

### Admin Endpoints
```
POST   /api/brands
POST   /api/laptops
PUT    /api/laptops/{id}
DELETE /api/laptops/{id}
```

### Customer – Cart
```
POST   /api/cart/add
PUT    /api/cart/update
DELETE /api/cart/remove/{itemId}
GET    /api/cart
```

### Customer – Orders
```
POST /api/orders/checkout
GET  /api/orders
POST /api/orders/{orderId}/cancel
```

### Customer – Payments
```
POST /api/payments/orders/{orderId}
```

---

## Security

- Role-based authorization using `@PreAuthorize`
- Unauthorized access → `401 Unauthorized`
- Forbidden access → `403 Forbidden`
- Unsupported HTTP method → `405 Method Not Allowed`

---

## Exception Handling

- Centralized `GlobalExceptionHandler`
- Handles:
  - Resource not found
  - Invalid IDs
  - Empty cart
  - Out-of-stock items
  - Duplicate entities
  - Unsupported HTTP methods

---

## How to Test Using Postman

1. Authenticate using **Basic Auth**
   - CUSTOMER for cart/orders/payments
   - ADMIN for brands/laptops
2. Create brands and laptops (ADMIN)
3. Search laptops (PUBLIC)
4. Add laptops to cart (CUSTOMER)
5. Checkout cart to create order
6. Initiate payment using POST endpoint

---

## Database

- MySQL
- JPA/Hibernate for ORM
- Relationships:
  - Brand → Laptops
  - Cart → CartItems → Laptop
  - Order → OrderItems → Laptop
  - Order → Payment

---

## Logging

- Log4j2 enabled
- Logs important application events:
  - Order creation
  - Payment initiation
  - Validation failures

---

## Current Status

✔ Brand Module  
✔ Laptop Module  
✔ Cart Module  
✔ Order Module  
✔ Payment Initiation  
✔ Security & Exception Handling  

---

## Planned Enhancements

- JWT authentication
- Payment status updates
- Admin order management
- Pagination & sorting
- Unit and integration tests
