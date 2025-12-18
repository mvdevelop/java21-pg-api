
#!/bin/sh

# Configurar porta (Render usa variável PORT)
PORT=${PORT:-8080}

echo "Iniciando aplicação na porta: $PORT"

# Iniciar Spring Boot
exec java \
  -Xmx256m -Xms128m \
  -XX:+UseContainerSupport \
  -Djava.security.egd=file:/dev/./urandom \
  -Dspring.main.lazy-initialization=true \
  -Dspring.main.banner-mode=off \
  -jar app.jar \
  --server.port=$PORT \
  --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}
  