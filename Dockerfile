
# Stage 1: Build
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar arquivos de build para o container
COPY pom.xml .
COPY src ./src

# Rodar o build com Maven (download dependências + build)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar o jar buildado da stage anterior
COPY --from=build /app/target/*.jar app.jar

# Expor a porta padrão da sua API (ajuste se necessário)
EXPOSE 8081

# Rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
