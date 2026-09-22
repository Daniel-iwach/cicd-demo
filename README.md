# Task Manager API

Spring Boot REST API with a CI/CD pipeline using GitHub Actions.

A practical project focused on backend development, automated testing, build processes, and continuous integration. The domain (a simple task manager) is intentionally minimal — the goal of this repo is to practice a clean layered architecture, testing, and a working CI/CD pipeline, not the business logic itself.

## Tech stack

- Java 21
- PostgreSQL
- springdoc-openapi (Swagger UI)
- Lombok
- JUnit 5 + Mockito + MockMvc
- H2 (in-memory DB used only for tests)
- Maven
- GitHub Actions

## Project structure

```
src/main/java/com/example/cicd_demo
├── configuration/           # OpenAPI (Swagger) configuration
├── controller/        # REST controllers
├── service/           # Service interfaces
│   └── impl/           # Service implementations
├── repository/        # Spring Data JPA repositories
├── model/             # JPA entities and enums
├── dto/               # Request/response DTOs
└── exception/          # Custom exceptions and global error handling

src/test/java/com/dani/taskmanager
├── service/           # Unit tests (Mockito)
└── controller/        # Web layer tests (@WebMvcTest + MockMvc)
```

## Endpoints

Base path: `/api/v1/tasks`

| Method | Path                     | Description                          |
|--------|--------------------------|---------------------------------------|
| POST   | `/api/v1/tasks`          | Create a task                        |
| GET    | `/api/v1/tasks`          | List all tasks (optional `?status=`) |
| GET    | `/api/v1/tasks/{id}`     | Get a task by id                     |
| PUT    | `/api/v1/tasks/{id}`     | Update a task                        |
| DELETE | `/api/v1/tasks/{id}`     | Delete a task                        |

Task status values: `PENDING`, `IN_PROGRESS`, `COMPLETED`.

## Running locally

### Option 1: Local Postgres + Maven

1. Start a local PostgreSQL instance and create a database:

   ```sql
   CREATE DATABASE taskmanager;
   ```

2. Set environment variables if your credentials differ from the defaults (`postgres` / `postgres`):

   ```bash
   export DB_HOST=localhost
   export DB_PORT=5432
   export DB_NAME=taskmanager
   export DB_USERNAME=postgres
   export DB_PASSWORD=postgres
   ```

3. Run the app:

   ```bash
   mvn spring-boot:run
   ```

## API documentation (Swagger)

Once the app is running:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Running tests

Tests use H2 in-memory database and Mockito, so no external database is needed:

```bash
mvn test
```

- `service` package: unit tests for the business logic, with the repository mocked.
- `controller` package: web layer tests using `@WebMvcTest` and `MockMvc`, with the service mocked.

## Building a jar

```bash
mvn clean package
```

The runnable jar will be at `target/task-manager-api.jar`.

## CI/CD (GitHub Actions)

Workflow file: `.github/workflows/ci-cd.yml`

On every push or pull request to `main`, the pipeline:

1. Checks out the repository.
2. Sets up JDK 21 with Maven dependency caching.
3. Compiles the project.
4. Runs the automated test suite.
5. Packages the application into a jar and uploads it as a build artifact.
6. On pushes to `main`, builds the Docker image as a validation step (not pushed to any registry by default).

This is meant as a learning setup — extending it with deployment (e.g. pushing the image to a registry, deploying to a server or cloud provider) is the natural next step.

## License

MIT
