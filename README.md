# Payment API
- This is a Payment REST API build with Java 17, Spring Boot 4, yaml config and Maven and jar packaging using [Spring Initializr](https://start.spring.io/)
- The application layers are controller (use cases), domain (entities), and repository (data access layer)
- There are DTOs representing both the request and response of HTTP calls
- It exposes the /api/v1/payments endpoint with GET, POST, PATCH, and DELETE operations
- The business rules for each endpoint is in the end-to-end test suit
- The application runs locally in localhost 8080
- The swagger openapi spec is in [Swagger](http://127.0.0.1:8080/swagger-ui/index.html#/)
- Exportable yaml swagger is in [Exportable YAML Swagger](http://127.0.0.1:8080/v3/api-docs.yaml)