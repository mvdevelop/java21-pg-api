
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar JAR
COPY --from=build /app/target/*.jar app.jar

# Render usa variável PORT, default 10000 mas podemos forçar 8080
ENV PORT=8080
EXPOSE $PORT

# Otimizações JVM para iniciar mais rápido
ENV JAVA_OPTS="-Xmx256m -Xms128m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Forçar porta 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=8080 --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
