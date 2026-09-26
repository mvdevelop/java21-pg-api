
# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN apk add --no-cache maven && mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Security: non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar

EXPOSE 8080

CMD java \
    -Xmx256m -Xms128m \
    -XX:+UseContainerSupport \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.main.lazy-initialization=true \
    -Dspring.main.banner-mode=off \
    -Dspring.profiles.active=prod \
    -Dserver.port=8080 \
    -jar app.jar
