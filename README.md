# Reverso Social – Backend

This repository contains the **backend for the Reverso Social project**, developed with **Java and Spring Boot**.
It provides the RESTful API that manages data, authentication, business logic, and email notifications.

---

## Tech Stack

### Backend

| Technology       | Version   | Purpose                        |
|------------------|-----------|--------------------------------|
| **Java**         | 21        | Programming Language           |
| **Spring Boot**  | 3.4.12    | Main Framework                 |
| **PostgreSQL**   | -         | Relational Database            |
| **Spring Security**| -       | Security and Authentication    |
| **JWT**          | 4.4.0     | Tokens (Auth0)                 |
| **MapStruct**    | 1.5.5     | Object Mapping                 |
| **Lombok**       | 1.18.34   | Boilerplate Reduction          |

### Frontend (Reference)

| Technology       | Version   | Purpose                        |
|------------------|-----------|--------------------------------|
| **React**        | ^18.2.0   | Main UI Library                |
| **Vite**         | ^7.2.4    | Bundler and Dev Environment    |
| **SASS**         | ^1.96.0   | CSS Preprocessor               |
| **React Router** | ^7.10.1   | Routing                        |
| **Axios**        | ^1.13.2   | HTTP Client                    |
| **Vitest**       | ^4.0.15   | Testing Framework              |

---

## Project Structure

 The main source code structure in `src/main/java/com/reverso/` is organized by layers:

```
com.reverso
│
├── config/          # Configurations (Security, CORS, Swagger, etc.)
├── controller/      # REST API Endpoints
├── dto/             # Data Transfer Objects (Request/Response)
│   ├── request/
│   └── response/
├── exception/       # Global exception handling
├── mapper/          # Mapping interfaces (MapStruct)
├── model/           # JPA Entities (Database)
├── repository/      # Data access interfaces (Spring Data JPA)
├── security/        # Security configurations (JWT, Filters)
├── service/         # Business Logic
│   ├── impl/        # Service implementations
│   └── interfaces/  # Service interfaces
│
└── ReversoSocialBeApplication.java  # Main class
```

---

## Available Scripts

In the root directory, you can execute (using `mvnw` on Windows or `./mvnw` on Linux/Mac):

### `mvnw clean install`
Cleans the project, compiles, and generates the `.jar` artifact.

### `mvnw spring-boot:run`
Starts the development server locally (default port 8080).

### `mvnw clean compile`
Forces recompilation (useful if there are MapStruct errors).

---

## Institutional Email Configuration

The system is configured to send confirmation emails (to the user) and notifications (to the admin) using SMTP. It currently supports **Gmail** and **Outlook**.

### Setup Steps (Gmail)

1.  **Generate App Password:**
    *   Go to your Google Account > Security > 2-Step Verification.
    *   Generate a new "App Password".
    *   Copy the 16-character code.

2.  **Configure Environment Variables (`.env`):**
    Create a `.env` file in the root with the following:

    ```properties
    # Sending credentials
    MAIL_USERNAME=your-email@gmail.com
    MAIL_PASSWORD=your-16-char-app-password

    # Email to receive notifications
    ADMIN_EMAIL=admin@reversosocial.com
    ```

3.  **Verify `application.properties`:**
    Ensure the active section is for Gmail:
    ```properties
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    ```

---

## Deployment Instructions

To deploy the application in a production environment (or local server):

1. **Prerequisites:**
   - Java 21 (JDK) installed.
   - Active PostgreSQL database.
   - Required environment variables configured (DB, JWT, MAIL).

2. **Build the artifact:**
   Run the following command to generate the `.jar` file:
   ```bash
   mvnw clean package -DskipTests
   ```
   The file will be generated in the `target/` folder with a name similar to `reverso-social-be-0.0.1-SNAPSHOT.jar`.

3. **Run the application:**
   You can run the `.jar` directly with Java:
   ```bash
   java -jar target/reverso-social-be-0.0.1-SNAPSHOT.jar
   ```

   *Note: Ensure environment variables are loaded in the system or passed to the command.*

---

## Team

Collaborative backend for **Reverso Social**.

| Name | GitHub | LinkedIn |
|--------|--------|----------|
| **Angela Bello** | [@AngelaBello-creator](https://github.com/AngelaBello-creator) | [Angela Bello](https://www.linkedin.com/in/angela-bello-developer/) |
| **Andrea Olivera** | [@andreaonweb](https://github.com/andreaonweb) | [Andrea Olivera Romero](https://www.linkedin.com/in/AndreaOliveraRomero) |
| **Gabi Gallegos** | [@hgall3](https://github.com/hgall3) | [Gabriela Gallegos Anda](https://www.linkedin.com/in/gabrielagallegosanda/) |
| **Erika Montoya** | [@DevErika](https://github.com/DevErika) | [Erika Montoya](https://www.linkedin.com/in/erikamontoya/) |
| **Luisa Moreno** | [@LuMorenoM](https://github.com/LuMorenoM) | [Luisa Moreno](https://www.linkedin.com/in/luisa-moreno-474334338/) |

---

## 📄 License

This project is for internal use for the team's academic/professional development and does not have a public license.
