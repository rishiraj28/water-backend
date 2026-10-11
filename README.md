# Water Quality Monitoring Backend

Spring Boot 3.3 service using Java 17, Maven, Spring Data MongoDB, and MongoDB Atlas.

## Deploy on Render

Create a Render Blueprint from this repository and use `render.yaml`. Create a MongoDB Atlas M0 cluster, add the Render service's outbound IP ranges to the Atlas network access list, then set the following values in the Render service environment. Do not commit them:

- `MONGODB_URI`: Atlas connection URI including the `wqm` database name
- `ADMIN_USERNAME` and `ADMIN_PASSWORD`: dedicated administrator credentials; use a unique, long password
- `APP_CORS_ALLOWED_ORIGINS`: exact Vercel site origin, such as `https://your-project.vercel.app`, without a trailing slash

Render supplies `PORT`. The `/actuator/health` endpoint is used as the service health check. The public API accepts complaint submissions; complaint reads, updates, and user-management endpoints require administrator authentication.

## Run Locally

Create `src/main/resources/application.properties` from `src/main/resources/application.properties.example`, set `MONGODB_URI` to `mongodb://localhost:27017/wqm`, and set `ADMIN_USERNAME` and `ADMIN_PASSWORD` in the shell. Then run:

```sh
sh ./mvnw spring-boot:run
```

The default local port is `8081`. The actual `application.properties` file is ignored by Git. Never commit database or administrator credentials.

## Tests

```sh
sh ./mvnw test
```

Tests mock the Mongo repository and do not require a running MongoDB server or production credentials.