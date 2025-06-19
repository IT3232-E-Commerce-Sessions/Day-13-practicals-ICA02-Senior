# IT3232 – E-Commerce Practical ICA02 - Day 13

This repository contains the solution for Day 13 practical session of the IT3232 E-Commerce course. The solution implements an e-commerce API with product search by manufacturing district, order placement with JSON validation, and stock quantity validation.

---

## Table of Contents

- [IT3232 – E-Commerce Practical ICA02 - Day 13](#it3232--e-commerce-practical-ica02---day-13)
  - [Table of Contents](#table-of-contents)
  - [Project Overview](#project-overview)
  - [Features](#features)
  - [Requirements](#requirements)
  - [Setup Instructions](#setup-instructions)
    - [Prerequisites](#prerequisites)
    - [Installation Steps](#installation-steps)
  - [Database Configuration](#database-configuration)
  - [API Documentation](#api-documentation)
    - [Base URL](#base-url)
    - [Endpoints](#endpoints)
  - [Sample Requests \& Responses](#sample-requests--responses)
    - [1. Search Products by District](#1-search-products-by-district)
    - [2. Place Order (Success)](#2-place-order-success)
    - [3. Place Order (Insufficient Stock)](#3-place-order-insufficient-stock)
    - [4. Place Order (Missing Data)](#4-place-order-missing-data)
  - [Project Structure](#project-structure)
  - [Exception Handling](#exception-handling)
  - [Contributing](#contributing)

---

## Project Overview

This Spring Boot application provides RESTful APIs for:

- Searching products by manufacturing district
- Placing orders with JSON input
- Validating product stock before order processing
- Returning structured error messages for validation failures

---

## Features

- **Product Search by District:** Filter products based on manufacturing location
- **Order Placement:** Process orders with customer ID, date, and product details
- **Stock Validation:** Ensure ordered quantities don't exceed available stock
- **Error Handling:** Structured JSON error responses for validation failures
- **JPA Repository:** Custom queries for product filtering

---

## Requirements

The solution implements the following requirements from the practical question paper:

- **Search products by manufacturing district (20 marks)**
    - Implement endpoint to filter products by district
- **Order placement with JSON input (40 marks)**
    - Accept orders with customer ID, date, product IDs and quantities
    - Validate required fields
    - Use JSON format as specified in Figure 1
- **Stock validation and error handling (25 marks)**
    - Validate ordered quantity against available stock
    - Return 406 error with structured JSON when stock is insufficient
    - Format error messages as shown in Figure 2

---

## Setup Instructions

### Prerequisites

- Java 17
- MySQL 8.0+
- Maven 3.8+

### Installation Steps

1. **Clone the repository:**
     ```bash
     git clone https://github.com/yourusername/it3232-day13-practical.git
     cd it3232-day13-practical
     ```

2. **Create MySQL database:**
     ```sql
     CREATE DATABASE Palmyra;
     ```

3. **Update database credentials in `src/main/resources/application.properties`:**
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/Palmyra
     spring.datasource.username=yourusername
     spring.datasource.password=yourpassword
     ```

4. **Build and run the application:**
     ```bash
     mvn spring-boot:run
     ```
     The application will be available at: [http://localhost:8080](http://localhost:8080)

---

## Database Configuration

Run these SQL commands to set up the required district data:

```sql
ALTER TABLE seller ADD COLUMN district VARCHAR(255);
UPDATE seller SET district = 'Jaffna' WHERE id = 'S001';
UPDATE seller SET district = 'Kilinochchi' WHERE id = 'S002';
UPDATE seller SET district = 'Mullaitivu' WHERE id = 'S003';
```

---

## API Documentation

### Base URL

```
http://localhost:8080/pro
```

### Endpoints

| Method | Endpoint              | Description                          |
|--------|----------------------|--------------------------------------|
| GET    | /district/{district} | Get products by manufacturing district |
| POST   | /                    | Place a new order                    |

---

## Sample Requests & Responses

### 1. Search Products by District

**Request:**
```http
GET /pro/district/Jaffna
```

**Response:**
```json
[
    {
        "id": 101,
        "name": "Palmyrah Boiled Tuber Chips",
        "price": 250.00,
        "stock": 50,
        "seller": {
            "id": "S001",
            "name": "Jaffna Foods",
            "district": "Jaffna"
        }
    },
    {
        "id": 102,
        "name": "Palmyrah Jaggery",
        "price": 350.00,
        "stock": 30,
        "seller": {
            "id": "S001",
            "name": "Jaffna Foods",
            "district": "Jaffna"
        }
    }
]
```

### 2. Place Order (Success)

**Request:**
```http
POST /pro
Content-Type: application/json

{
    "customerId": "C001",
    "date": "2023-08-08",
    "items": [
        {"productId": 102, "qty": 1},
        {"productId": 105, "qty": 1}
    ]
}
```

**Response:**
```
HTTP Status: 200 OK
"Your order placed"
```

### 3. Place Order (Insufficient Stock)

**Request:**
```http
POST /pro
Content-Type: application/json

{
    "customerId": "C001",
    "date": "2023-08-08",
    "items": [
        {"productId": 102, "qty": 100}
    ]
}
```

**Response:**
```json
{
    "code": 406,
    "status": "NOT Acceptable",
    "message": "Do not have enough stock! Available stock for Palmyrah Jaggery is 30"
}
```

### 4. Place Order (Missing Data)

**Request:**
```http
POST /pro
Content-Type: application/json

{
    "customerId": "C001",
    "items": []
}
```

**Response:**
```json
{
    "code": 400,
    "status": "BAD REQUEST",
    "message": "Missing required data"
}
```

---

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── lk/
│   │       └── ac/
│   │           └── vau/
│   │               ├── controller/         # REST controllers
│   │               │   └── ProductController.java
│   │               ├── model/              # Entity classes
│   │               │   ├── Product.java
│   │               │   └── User.java
│   │               ├── repo/               # Repository interfaces
│   │               │   └── ProductRepo.java
│   │               ├── service/            # Service layer
│   │               │   └── ProductService.java
│   │               └── EcommerceApplication.java # Main class
│   └── resources/
│       └── application.properties          # Configuration
└── test/                                   # Test cases
```

---

## Exception Handling

The application provides structured error responses for:

- **400 Bad Request:** Missing required data or invalid input
- **404 Not Found:** Product not found
- **406 Not Acceptable:** Insufficient stock
- **500 Internal Server Error:** Unexpected server errors

**Error responses follow the format:**
```json
{
    "code": 406,
    "status": "NOT Acceptable",
    "message": "Detailed error message"
}
```

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Create a new Pull Request
