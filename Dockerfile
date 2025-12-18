
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar curl para health check interno
RUN apk add --no-cache curl

COPY --from=build /app/target/*.jar app.jar

# Script de entrada com health check
COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 8080

# Health check interno do container
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

ENTRYPOINT ["/start.sh"]
