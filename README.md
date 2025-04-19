# 📦 Incon Marketplace Backend
A Spring Boot-based REST API for managing an online marketplace. This project provides features for managing products, users, and sellers, with support for authentication and role-based access control.

---

## 📚 Table of Contents
- [About](#about)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Environment Variables](#environment-variables)
- [Database](#database)
- [Testing](#testing)
- [Useful Commands](#useful-commands)
- [Contributing](#contributing)
- [License](#license)

---

## 📖 About
The Incon Marketplace Backend is a RESTful API that supports:
- Product management (CRUD operations)
- User and seller management
- Role-based access control (Admin, Seller, Buyer)
- Integration with PostgreSQL for data persistence
- Secure endpoints with Spring Security and JWT authentication

---

## 🚀 Tech Stack
- Java 17+
- Spring Boot 3.4.4
- Spring Data JPA
- Spring Web
- Spring Security
- PostgreSQL
- Maven
- JUnit & Mockito (for testing)

---

## ✅ Prerequisites
Ensure the following are installed on your system:
- Java JDK 17+
- Maven
- Git
- PostgreSQL

---

## 🛠️ Getting Started
1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   cd your-repo-name
2. **Configure Environment Variables**
   Update the `src/main/resources/application.properties` file with your local database credentials. Below is an example configuration:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/inconDB
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   server.port=8080
3. **Install Dependencies & Build**
   Run the following command to install dependencies and build the project:
   ```bash
   ./mvnw clean install

## ▶️ Running the Application
1. **Run with Maven**
   ```bash
   ./mvnw spring-boot:run

## 🌐 API Endpoints
| Method | Endpoint                  | Description                  |
|--------|---------------------------|------------------------------|
| GET    | `/api/products`           | Get all products             |
| GET    | `/api/products/{id}`      | Get product by ID            |
| POST   | `/api/products`           | Create a new product         |
| PUT    | `/api/products/{id}`      | Update an existing product   |
| DELETE | `/api/products/{id}`      | Delete a product             |
| PATCH  | `/api/products/{id}/stock`| Update product stock         |
| PATCH  | `/api/products/{id}/price`| Update product price         |

For detailed API documentation, visit the Swagger UI (if configured) at `http://localhost:8080/swagger-ui/`.

## 🧠 Database
- **Database**: PostgreSQL
- **Schema**: Automatically created using JPA
- **Test Database**: H2 in-memory database (configured in `application-test.properties`)
For more details, refer to the [Database Schema Documentation](https://drive.google.com/file/d/1kt56xgGTW-o_9ut7zXexrEE6qr1CUITJ/view?usp=sharing).

## 🧪 Testing
Run the tests using:
```bash
    ./mvnw test
```
This will execute all unit and integration tests. Ensure that the test database is properly configured in `application-test.properties`.
Testing Framework: 
- JUnit 5
- Mockito



