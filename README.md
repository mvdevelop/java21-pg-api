
## Java 21 REST API with PostgreSQL and Swagger
A modern REST API built with Java 21, Spring Boot 3, PostgreSQL, and Swagger/OpenAPI for documentation. This project demonstrates best practices for building scalable and well-documented APIs.

## 🚀 Features
Java 21 - Latest LTS version with modern features

Spring Boot 3.2.0 - Rapid application development framework

PostgreSQL - Robust relational database

Swagger/OpenAPI 3 - Interactive API documentation

Spring Data JPA - Simplified database operations

HikariCP - High-performance connection pooling

Lombok - Reduced boilerplate code

Validation - Comprehensive input validation

Global Exception Handling - Consistent error responses

## 📋 Prerequisites
Java 21 or later

Maven 3.6+ or Gradle 7+

PostgreSQL 12+

Git (optional)

## 🛠️ Installation
1. Clone the repository
bash
git clone https://github.com/yourusername/java21-pg-api.git
cd java21-pg-api
2. Configure PostgreSQL
Create database and user:
sql
-- Access PostgreSQL
sudo -u postgres psql

-- Create database
CREATE DATABASE api_db;

-- Create user (optional, you can use 'postgres')
CREATE USER api_user WITH PASSWORD 'secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE api_db TO api_user;

-- Exit
\q
Or run the initialization script:
bash
psql -U postgres -f init.sql
3. Configure application properties
Edit src/main/resources/application.yml:

yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/api_db
    username: your_username       # Default: postgres
    password: your_password
4. Build and run the application
Using Maven:
bash
# Clean and package
mvn clean package

# Run the application
mvn spring-boot:run
Using the JAR file:
bash
java -jar target/api-java21-1.0.0.jar
🔧 Configuration
Database Configuration
The application uses Hibernate for ORM with the following key configurations:

yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update    # Options: none, validate, update, create, create-drop
    show-sql: true        # Show SQL in logs (development only)
    properties:
      hibernate:
        format_sql: true  # Pretty-print SQL
Swagger Configuration
Swagger UI is automatically configured and available at:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/api-docs

Customize OpenAPI configuration in OpenApiConfig.java.

📚 API Documentation
Once the application is running, access the interactive API documentation:

text
http://localhost:8080/swagger-ui.html
Available Endpoints
Method	Endpoint	Description
GET	/api/produtos	Get all products
GET	/api/produtos/{id}	Get product by ID
GET	/api/produtos/buscar?nome={nome}	Search products by name
POST	/api/produtos	Create a new product
PUT	/api/produtos/{id}	Update a product
DELETE	/api/produtos/{id}	Delete a product
🧪 Testing the API
Using cURL
Create a product:
bash
curl -X POST "http://localhost:8080/api/produtos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Laptop Dell XPS",
    "descricao": "High-performance laptop with 16GB RAM, 512GB SSD",
    "preco": 1299.99,
    "quantidade": 10
  }'
Get all products:
bash
curl "http://localhost:8080/api/produtos"
Get product by ID:
bash
curl "http://localhost:8080/api/produtos/1"
Search products by name:
bash
curl "http://localhost:8080/api/produtos/buscar?nome=Dell"
Update a product:
bash
curl -X PUT "http://localhost:8080/api/produtos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Laptop Dell XPS Updated",
    "descricao": "Updated description",
    "preco": 1199.99,
    "quantidade": 15
  }'
Delete a product:
bash
curl -X DELETE "http://localhost:8080/api/produtos/1"
Using Swagger UI
Navigate to http://localhost:8080/swagger-ui.html and use the interactive interface to test all endpoints.

📁 Project Structure
text
java21-pg-api/
├── src/main/java/com/exemplo/api/
│   ├── ApiApplication.java              # Main application class
│   ├── config/
│   │   └── OpenApiConfig.java           # Swagger/OpenAPI configuration
│   ├── controller/
│   │   └── ProdutoController.java       # REST controller
│   ├── dto/
│   │   ├── ProdutoDTO.java              # Request DTO
│   │   └── ProdutoResponseDTO.java      # Response DTO
│   ├── handler/
│   │   └── GlobalExceptionHandler.java  # Global exception handling
│   ├── model/
│   │   └── Produto.java                 # JPA entity
│   ├── repository/
│   │   └── ProdutoRepository.java       # Spring Data JPA repository
│   └── service/
│       └── ProdutoService.java          # Business logic layer
├── src/main/resources/
│   ├── application.yml                  # Application configuration
│   └── init.sql                         # Database initialization script
├── pom.xml                              # Maven dependencies
└── README.md
📊 Database Schema
Products Table
sql
CREATE TABLE produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);
🔍 Validation
The API includes comprehensive validation:

Name: Required, 3-100 characters

Description: Required, 10-500 characters

Price: Required, minimum 0.01

Quantity: Required, minimum 0

Example error response:

json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "errors": {
    "nome": "O nome é obrigatório",
    "preco": "O preço deve ser maior que zero"
  }
}
🐛 Troubleshooting
Common Issues
1. PostgreSQL Connection Error
Error: FATAL: password authentication failed for user
Solution:

Verify PostgreSQL is running: sudo systemctl status postgresql

Check credentials in application.yml

Test connection: psql -U postgres -h localhost

2. Port Already in Use
Error: Web server failed to start. Port 8080 was already in use.
Solution:

Change port in application.yml

Kill the process: sudo fuser -k 8080/tcp

Use a different port: server.port=8081

3. Database Schema Issues
Error: Relation "produtos" does not exist
Solution:

Set ddl-auto: create or ddl-auto: create-drop temporarily

Run the init.sql script manually

🧹 Development
Running Tests
bash
mvn test
Code Formatting
bash
mvn spotless:apply
Checking Dependencies
bash
mvn dependency:tree
📦 Deployment
Build for Production
bash
mvn clean package -DskipTests
Docker Support (Optional)
Dockerfile:
dockerfile
FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/api-java21-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
Build and run with Docker:
bash
docker build -t java21-api .
docker run -p 8080:8080 java21-api
🚀 Production Considerations
Security: Enable HTTPS, use environment variables for sensitive data

Database: Use connection pooling, proper indexes, and regular backups

Monitoring: Add health checks, metrics, and logging

Performance: Enable caching, optimize queries, use pagination

Validation: Add rate limiting and request validation

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

👥 Contributing
Fork the repository

Create a feature branch: git checkout -b feature/amazing-feature

Commit changes: git commit -m 'Add amazing feature'

Push to branch: git push origin feature/amazing-feature

Open a Pull Request

🙏 Acknowledgments
Spring Boot

PostgreSQL

Swagger/OpenAPI

Hibernate

Lombok

Happy Coding! 🚀

For any questions or issues, please open an issue in the GitHub repository.
