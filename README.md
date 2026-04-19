# Job Offers - Recruitment Aggregator
A recruitment system based on a modular architecture that automates the process of gathering and managing job offers from multiple external sources.


## Quick Start with Docker
The easiest way to run the application and all its dependencies is using Docker Compose. 

Have Docker Desktop installed and running.

Clone the repository:

```bash
git clone https://github.com/Mikel99M/job-offers.git
cd job-offers

```

Build and start all services:

```Bash
docker-compose up --build
```
Access the application:

Backend API: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui/index.html



## Technical Stack
Backend: Java 17, Spring Boot 3.x

Security: Spring Security + JWT (Stateless Authentication)

Database: MongoDB (Persistent storage for job offers and users)

Caching: Redis (Performance optimization for frequently accessed offers)

Testing: JUnit 5, Mockito, MockMvc, Testcontainers (Mongo/Redis), WireMock (External API Mocking)

DevOps: Docker, Docker Compose

## Features
Automated Aggregation: Scheduled fetching of job offers from external providers via REST API.

Resilience & Error Handling: Fault-tolerant integration patterns ensuring system stability when external services are down.

User Management: Secure registration and login flow with JWT token-based authorization.

Offer Management: Features for adding, retrieving, and deactivating job offers with duplicate detection.

Advanced Caching: Optimized offer retrieval using Redis with automatic cache eviction on data updates.

## Architecture & Deployment
The project follows Hexagonal Architecture (Ports and Adapters) to ensure high maintainability and isolation of business logic from infrastructure.

## Infrastructure Layout:

Backend: Java Spring Boot application with modular domain structure.

External Integration: Mocked environment for testing external JSON providers using WireMock.

Storage: MongoDB for persistent job offer and user data.

Cache: Redis for efficient offer aggregation and reduced database load.

Deployment: Fully containerized with Docker
