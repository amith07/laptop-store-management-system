# 🖥️ Laptop Store Management System

A Spring Boot–based RESTful backend application designed to manage a laptop e-commerce platform.  
The system supports catalog management, role-based access control, search and filtering, and cart functionality.

This project is developed as a **capstone-level backend system**, following clean architecture and enterprise best practices.

---

## 🚀 Tech Stack

- **Java:** 17  
- **Spring Boot:** 3.5.x  
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **Hibernate**
- **MySQL**
- **Maven**
- **Postman** (API testing)

---

## 📦 Modules Implemented

### 1️⃣ Brand Management
- Create and list brands
- Soft delete support
- Validation and centralized exception handling
- Public read access

---

### 2️⃣ Laptop Management
- Create laptops under brands
- Update laptop specifications and pricing
- Independent stock management
- Soft delete and restore functionality
- Automatic status calculation:
  - `AVAILABLE`
  - `OUT_OF_STOCK`
- Public laptop listing
- Role-based access for write operations

---

### 3️⃣ Search & Filters
- Dynamic laptop search using JPA Specifications
- Filter by:
  - Brand
  - CPU
  - Price range
  - Availability status
- Pagination and sorting support
- Publicly accessible search API (no authentication required)

---

### 4️⃣ Cart Module
- One active cart per customer
- Add laptop to cart
- Quantity merging for same laptop
- Stock validation before adding items
- Price snapshot preserved at add-to-cart time
- Automatic subtotal and cart total calculation
- View cart contents
- Cart persistence using database storage

---

## 🔐 Security & Roles

The application currently uses **Spring Security with Basic Authentication** (temporary setup).

### Roles Implemented

| Role | Permissions |
|----|----|
| ADMIN | Full access (brands, laptops, cart) |
| MANAGER | Stock update operations |
| CUSTOMER | Cart operations |
| PUBLIC | Laptop listing and search |

### Test Users (In-Memory)

| Username | Password | Role |
|--------|----------|------|
| admin | admin123 | ADMIN |
| manager | manager123 | MANAGER |
| customer1 | customer123 | CUSTOMER |

---

## 🧪 API Testing

- All APIs are tested using **Postman**
- Public endpoints do not require authentication
- Protected endpoints enforce role-based access
- Proper HTTP status codes are returned for all scenarios

---

## ⚠️ Error Handling

- Centralized global exception handling
- Domain-specific error codes such as:
  - `BRAND_NOT_FOUND`
  - `LAPTOP_NOT_FOUND`
  - `SERIAL_EXISTS`
  - `INSUFFICIENT_STOCK`
  - `CART_EMPTY`
- Clear and consistent error responses
- Correct HTTP status mapping (400, 401, 403, 404, 409)

---

## 📌 Current Status

✅ Brand module completed  
✅ Laptop lifecycle fully implemented  
✅ Public search and filtering enabled  
✅ Cart functionality implemented  
✅ Security and role enforcement validated  

---

## 🔜 Planned Enhancements

- Update and remove cart items
- Checkout and order module
- JWT-based authentication
- Unit and integration testing
- Payment simulation
- Deployment configuration

---

## 👨‍💻 Author

**Amith R**  
Capstone Project – Laptop Store Management System  
