# Gestio Sanitaria – Nurse Management API

A REST API built with Spring Boot to manage the nurses of a hospital. It lets you create, read, update and delete nurse records (CRUD) and check nurse logins with a username and password.

## Description

Gestio Sanitaria keeps the hospital's nurse records in a MySQL database and exposes them through HTTP endpoints, so any front end or tool (such as Postman) can work with them. It solves two needs:

- **Nurse management:** create, find, update and delete nurses.
- **Nurse login:** check whether a username and password are correct, returning a proper HTTP status code.

Passwords can be sent to the API (to create a nurse or to log in), but they are never returned in any response.

### Technologies

| Technology | Version |
| --- | --- |
| Java | 17 or later |
| Spring Boot | 4.1.1 |
| Spring Data JPA / Hibernate | 7.x |
| MySQL | 8.0 |
| Maven | Wrapper included (`mvnw`) |
| JUnit 5 + Mockito | Unit tests |
| GitHub Actions | Continuous integration |

### Project structure

```
src/main/java/com/example/Gestio_Sanitaria
├── GestioSanitariaApplication.java   # Entry point
├── config/DataLoader.java            # Loads initial data when the table is empty
├── controller/NurseController.java   # REST endpoints under /nurse
├── controller/GeneralController.java # Home page (/)
├── model/Nurse.java                  # Nurse entity (table "nurses")
└── repository/NurseRepository.java   # Database access (Spring Data JPA)

src/test/java/com/example/Gestio_Sanitaria
└── controller/NurseControllerTests.java  # Unit tests for every endpoint
```

## Installation

### Requirements

- **Java JDK 17** or later
- **Git**
- Access to a **MySQL 8** database (the project is set up for a remote MySQL server, but a local one also works)
- Optional: an IDE such as Eclipse or IntelliJ IDEA, and **Postman** to test the API

Maven does not need to be installed: the project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`).

### 1. Clone the repository

```bash
git clone https://github.com/DanielLabrada/Gestio_Sanitaria.git
cd Gestio_Sanitaria
```

### 2. Install the dependencies

```bash
# Linux / macOS
./mvnw clean install -DskipTests

# Windows
mvnw.cmd clean install -DskipTests
```

In Eclipse: **File → Import → Maven → Existing Maven Projects**, select the project folder and then **Maven → Update Project**.

### 3. Configure the database

The connection is set in `src/main/resources/application.properties`. The host, port, database name and user can be changed with environment variables, without editing the file:

| Variable | What it sets |
| --- | --- |
| `DB_HOST` | Database server |
| `DB_PORT` | Port |
| `DB_NAME` | Database name |
| `DB_USERNAME` | User |

The password is set in `spring.datasource.password` in the same file.

Hibernate creates or updates the `nurses` table automatically when the application starts (`spring.jpa.hibernate.ddl-auto=update`), so there is no SQL script to run.

## Usage

### Run the application

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

In Eclipse: right-click `GestioSanitariaApplication.java` → **Run As → Java Application**.

The API starts at `http://localhost:8080`. The console shows `Started GestioSanitariaApplication` when it is ready.

### Endpoints

All the endpoints start with `/nurse`.

| Method | URL | Description | Responses |
| --- | --- | --- | --- |
| `POST` | `/nurse/login` | Checks a nurse's username and password | `200` correct, `401` wrong credentials, `400` missing data |
| `POST` | `/nurse/create` | Creates a new nurse (also `/new`, `/save`, `/add`) | `201` created, `400` missing data or username already used |
| `GET` | `/nurse/index` | Returns all the nurses | `200` |
| `GET` | `/nurse/id/{id}` | Finds a nurse by id | `200` found, `404` not found |
| `GET` | `/nurse/name/{name}` | Finds a nurse by full name (not case sensitive) | `200` found, `404` not found |
| `PUT` | `/nurse/id/{id}` | Updates a nurse by id | `200` updated, `404` not found, `400` invalid data |
| `DELETE` | `/nurse/id/{id}` | Deletes a nurse by id | `200` deleted, `404` not found |

### Examples

**Log in**

```http
POST http://localhost:8080/nurse/login
Content-Type: application/json

{ "username": "cgomez", "password": "4821" }
```

Response: `200 OK` with `true`. With a wrong password: `401 Unauthorized` with `false`.

**Create a nurse**

```http
POST http://localhost:8080/nurse/create
Content-Type: application/json

{ "fullName": "Test Nurse", "username": "testnurse", "password": "1234" }
```

Response: `201 Created`

```json
{ "id": 33, "fullName": "Test Nurse", "username": "testnurse" }
```

**Update a nurse**

```http
PUT http://localhost:8080/nurse/id/33
Content-Type: application/json

{ "fullName": "Test Nurse Updated", "username": "testnurse" }
```

Response: `200 OK` with the updated nurse. If `password` is left out, the current password is kept.

**Find and delete**

```http
GET    http://localhost:8080/nurse/id/33
GET    http://localhost:8080/nurse/name/Test Nurse Updated
GET    http://localhost:8080/nurse/index
DELETE http://localhost:8080/nurse/id/33
```

The `DELETE` returns `200 OK` the first time and `404 Not Found` if it is repeated, because the nurse no longer exists.

### Run the tests

```bash
# Linux / macOS
./mvnw test

# Windows
mvnw.cmd test
```

`NurseControllerTests` covers every endpoint of `NurseController`, both the successful cases and the error codes. The tests use a mocked repository, so they do not need a database connection.

### Continuous integration

The workflow in `.github/workflows/maven.yml` runs on GitHub Actions every time code is pushed to any branch and on every pull request to `main`. It builds the project with JDK 21 and runs `mvn clean test`. The result appears in the pull request as a green tick when all the tests pass, or a red cross when one fails.