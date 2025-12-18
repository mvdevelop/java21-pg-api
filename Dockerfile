
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Comando que FUNCIONA no Render
CMD java \
    -Xmx256m -Xms128m \
    -XX:+UseContainerSupport \
    -Dspring.profiles.active=prod \
    -Dserver.port=8080 \
    -jar app.jar
    