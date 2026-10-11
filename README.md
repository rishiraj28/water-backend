# Water Quality Monitoring Backend

Spring Boot backend for the Water Quality Monitoring application. It uses Java 11, Maven, Spring Data JPA, and MySQL.

## Configure

Create `src/main/resources/application.properties` from `src/main/resources/application.properties.example`, then provide the database credentials through environment variables:

```sh
cp src/main/resources/application.properties.example src/main/resources/application.properties
export DB_URL=jdbc:mysql://localhost:3306/wqm
export DB_USERNAME=your_mysql_username
export DB_PASSWORD=your_mysql_password
```

The local `application.properties` file is ignored by Git. Do not commit database credentials or other secrets.

## Run

With MySQL running and the `wqm` database available:

```sh
sh ./mvnw spring-boot:run
```

The backend listens on port `8081` by default.