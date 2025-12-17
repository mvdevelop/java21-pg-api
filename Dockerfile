
# Use este Dockerfile na raiz
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar arquivos do Maven
COPY pom.xml .
COPY src ./src

# Instalar Maven
RUN apk add --no-cache maven \
    && mvn clean package -DskipTests

# Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR
COPY --from=builder /app/target/*.jar app.jar

# Usar variável PORT do Render
ENV PORT=8080
EXPOSE $PORT

# Otimização para Render (plano free)
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT} --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
