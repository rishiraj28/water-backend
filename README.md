# Water Quality Monitoring Backend

Spring Boot 3.3 service using Java 17, Maven, Spring Data JPA, and MySQL.

## Deploy on Render

Create a Render Blueprint from this repository and use `render.yaml`. Use a MySQL server reachable from Render (for example, your existing managed MySQL provider), then set the following values in the Render service environment. Do not commit them:

- `DB_URL`: JDBC URL including the `wqm` database, for example `jdbc:mysql://HOST:3306/wqm?sslMode=REQUIRED`
- `DB_USERNAME` and `DB_PASSWORD`: a dedicated MySQL account with access to the `wqm` database
- `ADMIN_USERNAME` and `ADMIN_PASSWORD`: dedicated administrator credentials; use a unique, long password
- `APP_CORS_ALLOWED_ORIGINS`: exact Vercel site origin, such as `https://your-project.vercel.app`, without a trailing slash

Render supplies `PORT`. The `/actuator/health` endpoint is used as the service health check. The public API accepts and validates complaint submissions; complaint reads, updates, and user-management endpoints require administrator authentication. Hibernate updates the schema on startup and preserves existing complaint rows.

## Run Locally

Create `src/main/resources/application.properties` from `src/main/resources/application.properties.example`, set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `ADMIN_USERNAME`, and `ADMIN_PASSWORD` in your local environment, and ensure the MySQL database `wqm` exists. Then run:

```sh
sh ./mvnw spring-boot:run
```

The default local port is `8081`. The actual `application.properties` file is ignored by Git. Never commit database or administrator credentials.

## Tests

```sh
sh ./mvnw test
```

Integration tests use H2 in MySQL compatibility mode and exercise complaint insert, retrieval, validation, and status update through the actual Spring API/repository without needing production credentials. To test against your MySQL server, run the backend with its local environment variables and use the API requests documented below.

## API Smoke Test

Submit a complaint with `POST http://localhost:8081/api/complaints/add` and JSON fields `name`, `address`, `zone`, `complaintType`, `phoneNumber`, `emailAddress`, and `description`. A successful database insert returns HTTP `201` with the generated complaint ID. Use the configured administrator's HTTP Basic credentials to fetch `GET /api/complaints/getAll` and update `PATCH /api/complaints/{id}/status` with `{"status":"in_progress"}` (or `pending` / `resolved`).

The local `application.properties` contains connection secrets and is ignored by Git. Keep database and administrator credentials in environment variables or hosting-provider secret settings.