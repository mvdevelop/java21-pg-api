
# Stage 1: Build

# Imagem base com JDK 21 (openjdk oficial early access)
FROM eclipse-temurin:21-jdk AS build

# Variável para versão Maven que vamos instalar
ARG MAVEN_VERSION=3.9.3
ARG USER_HOME_DIR="/root"
ARG SHA=bb756b7b5e9eab289de7f88acba2fa98f98d4dfc77f76b4b6cc2b2c54702e53a
ARG BASE_URL=https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries

# Instalando wget, curl, bash e unzip para o setup do Maven
RUN apt-get update && apt-get install -y wget curl bash tar

RUN curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    # && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha256sum -c - \
    && tar -xzf /tmp/apache-maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && rm -f /tmp/apache-maven.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

WORKDIR /app

# Copia pom.xml e src para a imagem
COPY pom.xml .
COPY src ./src

# Build do projeto (ignorar testes)
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
