# syntax=docker/dockerfile:1
ARG JAVA_VERSION=21

FROM gradle:jdk${JAVA_VERSION} AS build
USER root
WORKDIR /workspace
COPY . .
RUN gradle :bootJar --no-daemon

# Build a reproducible demo SQLite database from the SQL sources.
RUN apt-get update \
    && apt-get install -y --no-install-recommends sqlite3 \
    && sqlite3 database.db < schema.sql \
    && sqlite3 database.db < data.sql \
    && rm -rf /var/lib/apt/lists/*

FROM eclipse-temurin:${JAVA_VERSION}-jdk
WORKDIR /app
COPY --from=build /workspace/build/libs/implementation.jar app.jar
COPY --from=build /workspace/database.db database.db
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
