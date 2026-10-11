# Water Quality Monitoring Backend

Spring Boot 3.3 service using Java 17, Maven, Spring Data JPA, and MySQL.

## Deploy on Render

Create a Render Blueprint from this repository and use `render.yaml`. Provision a managed MySQL database separately, then set the following values in the Render service environment. Do not commit them:

- `DB_URL`: JDBC URL from the database provider, for example `jdbc:mysql://HOST:PORT/DATABASE?sslMode=REQUIRED`
- `DB_USERNAME` and `DB_PASSWORD`: database credentials
- `ADMIN_USERNAME` and `ADMIN_PASSWORD`: dedicated administrator credentials; use a unique, long password
- `APP_CORS_ALLOWED_ORIGINS`: exact Vercel site origin, such as `https://your-project.vercel.app`, without a trailing slash

Render supplies `PORT`. The `/actuator/health` endpoint is used as the service health check. The public API accepts complaint submissions; complaint reads, updates, and user-management endpoints require administrator authentication.

## Run Locally

Create `src/main/resources/application.properties` from `src/main/resources/application.properties.example`, configure the database variables above, and set `ADMIN_USERNAME` and `ADMIN_PASSWORD` in the shell. Then run:

```sh
sh ./mvnw spring-boot:run
```

The default local port is `8081`. The actual `application.properties` file is ignored by Git. Never commit database or administrator credentials.

## Tests

```sh
sh ./mvnw test
```

Tests use an in-memory H2 database and do not require the production MySQL credentials.