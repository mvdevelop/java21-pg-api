
# Stage 1: Build com Maven e JDK 21
FROM maven:3.9.3-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o pom.xml e a pasta src
COPY pom.xml .
COPY src ./src

# Build da aplicação (sem rodar testes)
RUN mvn clean package -DskipTests

# Stage 2: Imagem runtime com JRE 21
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o jar gerado da imagem de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
