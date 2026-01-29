I have reviewed your `pom.xml`. Since you have `spring-cloud-starter-gateway-server-webmvc` included in your dependencies, your project is actually **"Gateway-capable"** even if you haven't written specific routing code yet. This is a huge plus for your demo!

Here is the complete, professional **README.md** tailored exactly to your `pom.xml`, including the **Flyway** and **Gateway** sections.

---

# 📚 Library Management System - Backend

A high-performance, secure backend system built with **Spring Boot 4.0.2** and **Java 21**. This project implements a comprehensive solution for managing library resources, user authentication, and book reservations.

## 🛠️ Tech Stack & Architecture

* **Core Framework**: Spring Boot 4.0.2 (Spring 7)
* **Security**: Spring Security + JWT (jjwt 0.11.5)
* **Database**: MySQL 8.0 with **Flyway** for version-controlled schema migrations.
* **API Management**: Integrated **Spring Cloud Gateway (WebMvc)** for unified request routing.(currently operating as a monolith and not used this for current version of the project)
* **Persistence**: Spring Data JPA + Hibernate.
* **Infrastructure**: Jakarta EE 11 compliance and **JSpecify** for null-safety.
* **migration tool**: Flyway.

---

## 🚀 Key Features

### 1. Librarian Operations

* **library Management**: Full CRUD operations for books and categories and reservations and users.
* **Image Handling**: Robust file upload system for book covers with dynamic path resolution to the `uploads/` directory.

### 2. Security & Authentication

* **JWT Implementation**: Stateless authentication using JSON Web Tokens.
* **Method-Level Security**: Granular access control using `@PreAuthorize` to distinguish between `LIBRARIAN` and `MEMBER` roles.

### 3. Automated Business Logic

* **Smart Reservations**: Automatic calculation of due dates (7, 14, 21 days) based on membership tiers.
* **Reliable Persistence**: Transactional integrity for all reservation and update operations.

### 4. Database Schema Management (Flyway)

The project uses **Flyway** to ensure the database schema is always in sync across development environments.

* Migrations are located in `src/main/resources/db/migration`.
* Automatic schema creation and updates on application startup.

---

## 🌐 API Gateway Integration

Although currently operating as a monolith, the project includes the **Spring Cloud Gateway (WebMvc)** starter.

* **Forward Compatibility**: The system is ready to act as an entry point for future microservices.
* **Unified Entry**: Designed to centralize cross-cutting concerns like rate-limiting and global logging.

---

## 📁 Project Structure

```text
library-api/
├── src/main/java/          # Source code (Security, Config, Controllers, Services)
├── src/main/resources/
│   └── db/migration/       # Flyway SQL migration files
├── uploads/                # Dynamic storage for book covers
├── pom.xml                 # Maven dependencies (Spring Boot 4.0.2, Spring Cloud 2025)
└── README.md

```

---

## 🧪 Quick Start for Demo

1. **Database**: Create `library_db` in MySQL.
2. **Run**: `mvn spring-boot:run`.
3. **Upload Image**:
* Endpoint: `POST /api/books/{id}/upload-image`
* Body: `form-data`, Key: `file` (Type: **File**).


4. **Verification**: Access images via `http://localhost:8081/api/books/images/{filename}`.

---

## 🛡️ Best Practices

* **Clean Code**: Zero warnings using **JSpecify** for null-safety and **Method References** in security configurations.
* **Portability**: Dynamic path resolution logic ensures the `uploads/` folder is accessible whether the app is run from the root or the module folder.
* **Standardized REST**: Uses `204 No Content` for deletions and `201 Created` for new resources.

---
