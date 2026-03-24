# 📦 Product Service (E-Commerce Microservice)

## 🧾 Overview
The **Product Service** is a core microservice in an e-commerce system responsible for managing products, including creation, updates, retrieval, filtering, and soft deletion.

It is built using **Spring Boot**, follows **microservices architecture**, and exposes REST APIs documented with **OpenAPI (Swagger)**.

---

## 🚀 Tech Stack
- Java 17+
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Hibernate
- Lombok
- PostgreSQL / MySQL
- OpenAPI (Swagger - springdoc)
- Maven

---

## 🏗️ Architecture
- Layered architecture:
  - **Controller Layer** (REST APIs)
  - **Service Layer** (Business logic)
  - **Repository Layer** (Database access)
  - **DTO + Mapper Layer** (Data transformation)
- Soft delete implemented via status flag
- Validation and exception handling included

---

## 📂 Project Structure

com.ecom

<code>
├── controller
├── service
├── service.impl
├── repository
├── model
├── dto
├── mapper
├── exception
├── validation
├── endpoints
</code>

---

## ⚙️ Features

### 👤 User APIs
- Get product by ID
- Get active products (pagination + sorting)
- Get products by brand
- Get products by category
- Filter products using multiple criteria (keyword, price range, category, brand, status)

### 🛠️ Admin APIs
- Create product
- Update product
- Activate / Deactivate product
- Delete product (soft delete recommended)
- Get all products (with status filter)
- Pagination and sorting support

---

## 🔗 API Documentation (Swagger)
After running the application:
http://localhost:8081/swagger-ui/index.html


---

## ▶️ How to Run the Project

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/product-service.git
cd product-service

### 2. Configure Database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/product_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

### 3. Build the Project
mvn clean install
