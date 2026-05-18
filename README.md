# 📚 Book Library CRUD API

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue?style=for-the-badge&logo=gradle)](https://gradle.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Lombok](https://img.shields.io/badge/Lombok-Enabled-red?style=for-the-badge)](https://projectlombok.org/)

A highly structured, robust, and clean **Spring Boot RESTful CRUD API** for managing a library database of books. This project demonstrates enterprise-level development best practices, featuring Java 21 records, Spring Data JPA, comprehensive input validation, unified standard response modeling, generalized exception handling, SLF4J logging, and thoroughly written unit and controller slice tests.

---

## 🚀 Key Features

*   **⚡ Complete CRUD Capabilities**: Manage library books via CREATE, READ, UPDATE, and DELETE operations.
*   **📋 Data Validation**: JSR-380 validation (`@NotBlank`, `@Size`) is enforced on all input requests to maintain database integrity.
*   **🛡️ Robust Exception Handling**: Centralized, global exception interceptor (`@RestControllerAdvice`) transforming runtime errors and validation failures into elegant, user-friendly JSON models.
*   **💎 Standardized API Response Layout**: Unified response envelope (`ApiRes<T>`) complete with status codes, titles, localized messages, and payload data.
*   **📂 Modern Java Records**: Replaced traditional class boilerplate with modern **Java 21 Records** for immutable DTO layers.
*   **📝 Service & Controller Unit Tests**: Complete coverage utilizing JUnit 5, Mockito, and Spring MockMvc slice tests.
*   **🔍 Detailed Logging**: Step-by-step trace of request entries and service processes via SLF4J.

---

## 🛠️ Technology Stack

*   **Language**: Java 21 (OpenJDK)
*   **Framework**: Spring Boot 4.0.6 (Spring Web MVC, Spring Data JPA, Spring Validation)
*   **Database**: MySQL
*   **ORM**: Hibernate / JPA
*   **Build Tool**: Gradle (Kotlin/Groovy DSL)
*   **Utility & Testing**: Lombok, JUnit 5, Mockito, Spring Boot Starter Test

---

## 📂 Project Architecture

The codebase adheres strictly to the classic layered architectural pattern for high maintainability:

```text
com.tasks.crud
│
├── CrudApplication.java             # Spring Boot main entry point
│
├── Controller
│   ├── BookController.java          # REST API endpoints mapping
│   └── GlobalExceptionHandler.java  # Intercepts and customizes exceptions
│
├── Service
│   └── BookService.java             # Business logic layer
│
├── Repository
│   └── BookRepo.java                # JPA repository interface for MySQL interaction
│
├── Entity
│   └── Books.java                   # Hibernate Database Entity Mapping
│
├── DTO
│   ├── AddBookReq.java              # Request record for creating/updating a book
│   ├── BookReq.java                 # Request record including Book ID
│   ├── BookRes.java                 # Response record representing a book
│   └── ApiRes.java                  # Generic global API response layout
│
└── Util
    └── ResponseCodeUtil.java        # Standardized code dictionary
```

---

## ⚙️ Setup & Installation Instructions

Follow these steps to configure and run the application locally. If you need to use this repository, please fork the original repository and work on the correct branch.

### 🍴 Fork & Clone
1. Fork the repository from [PriyatharsanR/crud](https://github.com/PriyatharsanR/crud).
2. Clone your fork locally using the `main` branch:
   ```bash
   git clone -b main https://github.com/PriyatharsanR/crud.git
   cd crud
   ```

### 📋 Prerequisites
*   **Java Development Kit (JDK) 21** or higher installed.
*   **MySQL Server** running locally or via Docker.
*   **Gradle** (Optional: Gradle Wrapper `gradlew` is included in the root).

### 🗄️ Database Setup
1. Log in to your MySQL environment and create the target database named `libdb`:
   ```sql
   CREATE DATABASE libdb;
   ```
2. Update the credentials in the [application.properties](file:///c:/Users/ASUS/Documents/Tasks/crud/src/main/resources/application.properties) file if they differ from your local setup:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/libdb
   spring.datasource.username=your_mysql_user_name
   spring.datasource.password=your_mysql_password
   ```

### 🏃 Running the Application
Use the Gradle Wrapper to compile and start the local Spring Boot Web server:

**On Windows (Command Prompt / PowerShell)**:
```bash
# Compile and boot run
.\gradlew.bat bootRun
```

**On Unix (macOS / Linux)**:
```bash
# Grant execution permissions
chmod +x gradlew
# Compile and boot run
./gradlew bootRun
```
The server will initialize and begin listening on **`http://localhost:8080`**.

---

## 📊 Database Schema

The persistent layer maps to a `books` table automatically managed via Hibernate DDL.

#### Entity Model: `Books`
| Field Name | Data Type | Constraint / Key | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INT` | Primary Key, Auto-Increment | Unique identifier for each book |
| `title` | `VARCHAR(255)` | `NOT NULL` | The title of the book (max 100 chars in validation) |
| `author` | `VARCHAR(255)` | `NOT NULL` | The author of the book (max 100 chars in validation) |

---

## 🔌 API Endpoints Documentation

All endpoints are prefixed with the base path `/api/v1/books`.

### 1. Add a New Book
*   **Method**: `POST`
*   **Path**: `/api/v1/books`
*   **Status Code**: `201 Created`
*   **Request Payload**:
    ```json
    {
      "title": "Clean Code",
      "author": "Robert C. Martin"
    }
    ```
*   **Response Payload**:
    ```json
    {
      "id": 1,
      "title": "Clean Code",
      "author": "Robert C. Martin"
    }
    ```

---

### 2. Fetch All Books
*   **Method**: `GET`
*   **Path**: `/api/v1/books`
*   **Status Code**: `200 OK`
*   **Response Payload**:
    ```json
    [
      {
        "id": 1,
        "title": "Clean Code",
        "author": "Robert C. Martin"
      },
      {
        "id": 2,
        "title": "Refactoring",
        "author": "Martin Fowler"
      }
    ]
    ```

---

### 3. Fetch Book By ID
*   **Method**: `GET`
*   **Path**: `/api/v1/books/{id}`
*   **Status Code**: `200 OK` / `404 Not Found`
*   **Response Payload (`200 OK`)**:
    ```json
    {
      "code": "0000",
      "title": "Success",
      "message": "Book retrieved successfully",
      "data": {
        "id": 1,
        "title": "Clean Code",
        "author": "Robert C. Martin"
      }
    }
    ```
*   **Response Payload (`404 Not Found`)**:
    ```json
    {
      "code": "3001",
      "title": "NOT FOUND",
      "message": "Book not found with ID: 99",
      "data": null
    }
    ```

---

### 4. Update a Book
*   **Method**: `PUT`
*   **Path**: `/api/v1/books/{id}`
*   **Status Code**: `200 OK` / `400 Bad Request` / `404 Not Found`
*   **Request Payload**:
    ```json
    {
      "title": "Clean Code (2nd Edition)",
      "author": "Robert C. Martin"
    }
    ```
*   **Response Payload (`200 OK`)**:
    ```json
    {
      "code": "0000",
      "title": "Success",
      "message": "Book updated successfully",
      "data": {
        "id": 1,
        "title": "Clean Code (2nd Edition)",
        "author": "Robert C. Martin"
      }
    }
    ```

---

### 5. Delete a Book
*   **Method**: `DELETE`
*   **Path**: `/api/v1/books/{id}`
*   **Status Code**: `200 OK` / `404 Not Found`
*   **Response Payload (`200 OK`)**:
    ```json
    {
      "code": "0000",
      "title": "SUCCESS",
      "message": "Delete Book Successfully",
      "data": null
    }
    ```

---

## 🚫 Standard Response Code Dictionary

The application uses custom transaction response codes located in `ResponseCodeUtil` to enrich REST status codes:

| Custom Code | Title | HTTP Equivalent | Scenario |
| :--- | :--- | :--- | :--- |
| `0000` | `SUCCESS` / `Success` | `200 OK` / `201 Created` | Transaction completed successfully |
| `3000` | `VALIDATION FAILED` | `400 Bad Request` | Input fields violate validation constraints |
| `3001` | `NOT FOUND` | `404 Not Found` | Requested book ID does not exist in MySQL |
| `1010` | `FAILED` | `500 Internal Error` | Unexpected server-side exceptions |

#### Validation Error Format Example (`400 Bad Request`):
```json
{
  "code": "3000",
  "title": "VALIDATION FAILED",
  "message": "Input validation error",
  "data": {
    "title": "Title cannot be blank",
    "author": "Author cannot be blank"
  }
}
```



---

## 🧪 Testing and Verification

The project comes pre-configured with unit tests using Mockito framework and MockMvc Integration/Slice tests.

To run all tests and verify everything behaves flawlessly:
```bash
# Run tests
.\gradlew.bat test
```

A clean HTML report will be generated automatically inside the `build/reports/tests/test/index.html` directory.

---

## 🤝 Contribution Guidelines

1. Fork the repository from the main project:
   https://github.com/PriyatharsanR/crud

2. Clone your fork locally and ensure you are on the `main` branch:

   ```bash
   git clone -b main https://github.com/PriyatharsanR/crud.git
   cd crud
   ```

3. Create a feature branch from `main`:

   ```bash
   git checkout -b feature/your-feature-name
   ```

   Example:

   ```bash
   git checkout -b feature/book-search
   ```

4. Commit your changes with clear commit messages:

   ```bash
   git commit -m "Add book search feature"
   ```

5. Push your feature branch to GitHub:

   ```bash
   git push origin feature/your-feature-name
   ```

6. Open a Pull Request targeting the `main` branch of the parent repository.

---

---

## 📄 License
This project is open-source and licensed under the [MIT License](LICENSE).
