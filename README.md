# Community Platform Backend

**Java · Spring Boot · SQLite · SQL · REST · Spring JDBC · Docker**

This repository contains a cleaned portfolio version of an **individual university project** completed during my B.Sc. in Computer Science at Heinrich Heine University Düsseldorf in the module *Datenbanken: Weiterführende Konzepte*.

The project covers the path from **conceptual database design** to a runnable **REST backend** for a small community platform with citizens, groups, events, advertisements, messages, commercial providers, locations and ratings.

> **Project context:** The course provided a Spring application scaffold and technical requirements. I implemented the project-specific database model, relational schema, SQL logic and required REST functionality. Grading files, internal university infrastructure references and course-only artifacts were removed from this public version.

## What the Project Covers

- ER modeling and conversion to a relational schema
- SQLite database with **16 tables**
- primary and foreign keys, `CHECK` / `UNIQUE` constraints and referential integrity
- **4 database triggers** for integrity rules that cannot be expressed cleanly as simple column constraints
- manually written SQL queries with joins, filtering and aggregation
- Spring Boot REST backend with **21 endpoint mappings**
- controller / service / repository structure
- JDBC-based persistence using `JdbcTemplate`
- transaction handling for multi-step write operations
- Spring Security integration for authenticated and role-aware operations
- Docker-based application packaging

## Architecture

```text
Client
  │
  ▼
REST Controller
  │
  ▼
Service Layer
  │
  ▼
Repository / JdbcTemplate
  │
  ▼
SQLite Database
  │
  ├── Constraints
  ├── Triggers
  └── Relational Integrity
```

## Domain Model

The database represents a community platform with entities such as citizens, groups, events, advertisements, messages, commercial providers, locations, professions, skills, ratings and participation relationships.

A compact overview of the main relationships and integrity rules is available in [`docs/database-model.md`](./docs/database-model.md). The executable schema itself is defined in [`schema.sql`](./schema.sql).

## REST API

The project implements endpoint mappings for operations such as:

- listing and creating citizens and commercial providers
- querying locations and specializations
- filtering events, groups and advertisements
- creating events and groups
- creating, updating and deleting advertisements
- adding and deleting messages
- adding event ratings

The endpoint implementation is in [`RestController.java`](./src/main/java/de/hhu/cs/dbs/dbwk/project/controller/RestController.java).

## Database Design

The schema in [`schema.sql`](./schema.sql) contains **16 relational tables** and **4 triggers**. It uses foreign keys, composite keys, `CHECK` constraints and other integrity rules.

Examples of modeled rules include:

- citizens may only rate events they attended
- citizens can belong to at most two private groups
- advertisements with associated messages cannot be deleted
- group membership is capped through a database trigger

Example data is provided in [`data.sql`](./data.sql), while [`queries.sql`](./queries.sql) contains representative SQL queries. The schema, sample data and all three query examples were re-checked successfully with SQLite before publishing this portfolio version.

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2 |
| API | Spring Web / REST |
| Persistence | Spring JDBC, `JdbcTemplate` |
| Database | SQLite |
| Security | Spring Security |
| Build | Gradle |
| Containerization | Docker / Docker Compose |
| Modeling | ER model, relational model, SQL DDL/DML |

## Repository Structure

```text
.
├── docs/
│   └── database-model.md
├── src/main/java/.../
│   ├── controller/
│   ├── entities/
│   ├── model/
│   ├── persistence/
│   ├── presentation/
│   └── security/
├── schema.sql
├── data.sql
├── queries.sql
├── Dockerfile
├── compose.yaml
└── build.gradle.kts
```

## Running the Application

### Docker

```bash
docker compose up --build
```

The application is exposed on `http://localhost:8080`.

### Gradle

With Java 21 and Gradle installed:

```bash
gradle bootRun
```

For the Docker workflow, the demo SQLite database is generated from `schema.sql` and `data.sql` during the image build.

## Notes

This is an academic project and not a production-ready service. The focus was relational database design, SQL, integrity constraints and implementing the specified backend API. Security and deployment configuration should therefore be viewed in that context rather than as production hardening.

## Author

**Mohamad Yaman Alshallah**  
B.Sc. Computer Science — Heinrich Heine University Düsseldorf

GitHub: [@moals107](https://github.com/moals107)
