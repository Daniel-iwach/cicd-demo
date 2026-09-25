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
- Docker + Docker Compose
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

### Option 2: Docker Compose (app + database)

This starts both PostgreSQL and the API in containers. You don't need Java, Maven, or PostgreSQL installed — only [Docker](https://www.docker.com/products/docker-desktop/).

**Files involved:**

| File                 | Purpose                                                                                     |
|----------------------|---------------------------------------------------------------------------------------------|
| `Dockerfile`         | Multi-stage build: Maven compiles the jar, then a slim JRE 21 image runs it.               |
| `docker-compose.yml` | Defines two services: `db` (PostgreSQL 15) and `app` (the API), plus a volume for the data. |
| `.env`               | Environment variables read by the `app` service (database connection).                      |

**1. Create a `.env` file** in the project root (it is not committed to Git). Inside Docker, `DB_HOST` must be `db` (the service name in `docker-compose.yml`), not `localhost`, and the credentials must match the `POSTGRES_*` values in `docker-compose.yml`:

```env
DB_HOST=db
DB_PORT=5432
DB_NAME=taskmanager
DB_USERNAME=postgres
DB_PASSWORD=taskmanagerTest
```

**2. Build and start everything:**

```bash
docker compose up --build
```

The first run downloads the base images and compiles the project, so it can take a few minutes. Once you see `Started CicdDemoApplication`, the API is available at:

- API: `http://localhost:8080/api/v1/tasks`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

**Useful commands:**

```bash
docker compose up -d --build   # start in the background
docker compose ps              # list running containers
docker compose logs -f app     # follow the app logs
docker compose stop            # stop containers (keeps them and the data)
docker compose down            # stop and remove containers (keeps the data volume)
docker compose down -v         # stop and remove containers AND delete the database data
```

**Notes and troubleshooting:**

- After changing code or `pom.xml`, run `docker compose up --build` again to rebuild the image.
- Database data is stored in the `postgres_data` volume. The `POSTGRES_*` credentials are applied only the first time the volume is created; to change them, run `docker compose down -v` first.
- If port `5432` or `8080` is already in use (for example by a local PostgreSQL or by the app running from your IDE), stop that process or change the left-hand port in the `ports` section of `docker-compose.yml`.
- `depends_on` waits for the `db` container to start, not for PostgreSQL to be ready. If the app fails on the first start, `restart: always` retries automatically.

## API documentation (Swagger)

Once the app is running (with Maven or Docker):

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Running tests

Tests use H2 in-memory database and Mockito, so no external database is needed:

```bash
mvn test
```

- `service` package: unit tests for the business logic, with the repository mocked.

## Building a jar

```bash
mvn clean package
```

The runnable jar will be at `target/task-manager-api.jar`.

## CI/CD (GitHub Actions)

Workflow file: `.github/workflows/maven.yml`

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