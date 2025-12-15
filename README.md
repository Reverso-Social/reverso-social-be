# Reverso Social – Backend

Este repositorio contiene el **backend del proyecto Reverso Social**, desarrollado con **Java y Spring Boot**.
Provee la API RESTful que gestiona los datos, la autenticación, la lógica de negocio y el envío de notificaciones por correo electrónico.

---

## Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3.4.12**
- **Spring Security & JWT** (Autenticación)
- **PostgreSQL** (Base de datos)
- **Maven** (Gestión de dependencias)
- **Lombok** (Reducción de código repetitivo)
- **MapStruct** (Mapeo de entidades DTOs)

---

## Estructura del proyecto

La estructura principal del código fuente en `src/main/java/com/reverso/` está organizada por capas:

```
com.reverso
│
├── config/          # Configuraciones (Security, CORS, Swagger, etc.)
├── controller/      # Endpoints de la API REST
├── dto/             # Data Transfer Objects (Request/Response)
│   ├── request/
│   └── response/
├── exception/       # Manejo global de excepciones
├── mapper/          # Interfaces de mapeo (MapStruct)
├── model/           # Entidades JPA (Base de datos)
├── repository/      # Interfaces de acceso a datos (Spring Data JPA)
├── service/         # Lógica de negocio
│   ├── impl/        # Implementaciones de servicios
│   └── interfaces/  # Interfaces de servicios
│
└── ReversoSocialBeApplication.java  # Clase principal
```

---

## 🖥️ Scripts disponibles

En el directorio raíz puedes ejecutar (usando `mvnw` en Windows o `./mvnw` en Linux/Mac):

### `mvnw clean install`
Limpia el proyecto, compila y genera el artefacto `.jar`.

### `mvnw spring-boot:run`
Levanta el servidor de desarrollo localmente (por defecto en el puerto 8080).

### `mvnw clean compile`
Fuerza la recompilación (útil si hay errores con MapStruct).

---

## Stack Tecnológico

| Tecnología       | Versión   | Propósito                      |
|------------------|-----------|--------------------------------|
| **Java**         | 21        | Lenguaje de programación       |
| **Spring Boot**  | 3.4.12    | Framework principal            |
| **PostgreSQL**   | -         | Base de datos relacional       |
| **Spring Security**| -       | Seguridad y Autenticación      |
| **JWT**          | 4.4.0     | Tokens (Auth0)                 |
| **MapStruct**    | 1.5.5     | Mapeo de objetos               |
| **Lombok**       | 1.18.34   | Reducción de boilerplate       |

---

## 📧 Configuración de Correo Institucional

El sistema está configurado para enviar correos de confirmación (al usuario) y notificación (al administrador) utilizando SMTP. Actualmente soporta **Gmail** y **Outlook**.

### Pasos para configurar (Gmail)

1.  **Generar Contraseña de Aplicación:**
    *   Ve a tu Cuenta de Google > Seguridad > Verificación en dos pasos.
    *   Genera una nueva "Contraseña de aplicaciones".
    *   Copia el código de 16 caracteres.

2.  **Configurar Variables de Entorno (`.env`):**
    Crea un archivo `.env` en la raíz con lo siguiente:

    ```properties
    # Credenciales de envío
    MAIL_USERNAME=tu-correo@gmail.com
    MAIL_PASSWORD=tu-contraseña-de-aplicación-de-16-caracteres

    # Correo donde se reciben las notificaciones
    ADMIN_EMAIL=admin@reversosocial.com
    ```

3.  **Verificar `application.properties`:**
    Asegúrate de que la sección activa sea la de Gmail:
    ```properties
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    ```

---

## Instrucciones de Despliegue

Para desplegar la aplicación en un entorno de producción (o servidor local):

1. **Pre-requisitos:**
   - Tener Java 21 (JDK) instalado.
   - Tener una base de datos PostgreSQL activa.
   - Configurar las variables de entorno necesarias (DB, JWT, MAIL).

2. **Construir el artefacto:**
   Ejecuta el siguiente comando para generar el archivo `.jar`:
   ```bash
   mvnw clean package -DskipTests
   ```
   El archivo se generará en la carpeta `target/` con un nombre similar a `reverso-social-be-0.0.1-SNAPSHOT.jar`.

3. **Ejecutar la aplicación:**
   Puedes correr el `.jar` directamente con Java:
   ```bash
   java -jar target/reverso-social-be-0.0.1-SNAPSHOT.jar
   ```

   *Nota: Asegúrate de que las variables de entorno estén cargadas en el sistema o pasadas al comando.*

---

##  Equipo

Backend colaborativo del proyecto **Reverso Social**.

| Nombre | GitHub | LinkedIn |
|--------|--------|----------|
| **Angela Bello** | [@AngelaBello-creator](https://github.com/AngelaBello-creator) | [Angela Bello](https://www.linkedin.com/in/angela-bello-developer/) |
| **Andrea Olivera** | [@andreaonweb](https://github.com/andreaonweb) | [Andrea Olivera Romero](https://www.linkedin.com/in/AndreaOliveraRomero) |
| **Gabi Gallegos** | [@hgall3](https://github.com/hgall3) | [Gabriela Gallegos Anda](https://www.linkedin.com/in/gabrielagallegosanda/) |
| **Erika Montoya** | [@DevErika](https://github.com/DevErika) | [Erika Montoya](https://www.linkedin.com/in/erikamontoya/) |
| **Luisa Moreno** | [@LuMorenoM](https://github.com/LuMorenoM) | [Luisa Moreno](https://www.linkedin.com/in/luisa-moreno-474334338/) |

---

## Licencia

Este proyecto es de uso interno para desarrollo académico/profesional del equipo y no posee licencia pública.
