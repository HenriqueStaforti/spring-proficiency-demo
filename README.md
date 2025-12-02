# Spring Proficiency Demo

This project is a practical demonstration of proficiency within the **Spring Boot** ecosystem, bringing together several widely used components in corporate applications. It implements a **Product CRUD**, uses **PostgreSQL** as the relational database, **Redis** for caching, **RabbitMQ** for messaging, and integrates with an external service using **OpenFeign** for auditing purposes.

The goal is to showcase recommended practices in architecture, service integration, authentication/authorization mechanisms, messaging, caching, and external communication.

---

## 🧾 Technologies Used

- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Spring Data Redis**
- **OpenFeign**
- **RabbitMQ**
- **Flyway**
- **PostgreSQL**
- **Lombok**
- **MapStruct**
- **Docker + Docker Compose**
- **Maven**

---

## 📌 Main Features

### 🔐 Authentication & Authorization (Spring Security)
- User creation and login.
- JWT token generation.
- Every login or user creation triggers an external audit request via **OpenFeign**.

### 📦 Product CRUD
- Endpoints for creating, updating, listing, and deleting products.
- Persistence using **Spring Data JPA** with **PostgreSQL**.
- Migrations managed via **Flyway**.

### ⚡ Redis Cache
- Product **GET** operations use Redis caching to improve performance.
- Cache entries are automatically invalidated on updates.

### 📨 Messaging with RabbitMQ
- When a product is created, the system publishes a message to RabbitMQ.
- The service itself consumes the message for demonstration purposes.

---

## 🚀 How to Run the Project

### ⚙️ Requirements
- Docker
- Java 17+
- Maven 3.8+
- `.env` file with required environment variables (for docker-compose)

---

### ▶️ 1. Start Required Services

In the project root, run:

```bash
docker compose up -d
```

This will start:
- PostgreSQL
- Redis
- RabbitMQ

---

### ▶️ **2. Run the application**

```bash
mvn spring-boot:run
```

The API will be available at::

```
http://localhost:8080
```

---

## 🧪 **Main Endpoints**

### 🔐 **Authentication**
- `POST /auth/register` — Creates a new user (triggers an external audit request via Feign)
- `POST /auth/login` — Generates a JWT token (also triggers an audit request via Feign)

### 📦 **Products**
- `GET /products` — Lists products (with Redis cache)
- `POST /products` — Creates a product (publishes a RabbitMQ message)
- `GET /products/{id}` — Retrieves a specific product (with Redis cache)
- `PATCH /products/{id}` — Updates a product
- `DELETE /products/{id}` — Removes a product

---

## 📄 **License**

This project is licensed under the MIT License. Feel free to use it as a reference.

---

## 👤 **Author**
**Henrique Staforti**  
GitHub: https://github.com/HenriqueStaforti
