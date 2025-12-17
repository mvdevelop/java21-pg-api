
#!/bin/sh

# Configurações para Render
export JAVA_OPTS="-Xmx256m -Xms128m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Converter DATABASE_URL do Render se existir
if [ -n "$DATABASE_URL" ]; then
  # Formato: postgresql://user:password@host:port/database
  # Remover protocolo
  DB_CONN=${DATABASE_URL#postgresql://}
  
  # Extrair componentes
  USER_PASS=$(echo $DB_CONN | cut -d'@' -f1)
  HOST_DB=$(echo $DB_CONN | cut -d'@' -f2)
  
  USERNAME=$(echo $USER_PASS | cut -d':' -f1)
  PASSWORD=$(echo $USER_PASS | cut -d':' -f2)
  
  HOST=$(echo $HOST_DB | cut -d'/' -f1)
  DATABASE=$(echo $HOST_DB | cut -d'/' -f2)
  
  # Adicionar porta padrão se não tiver
  if [[ ! $HOST =~ :[0-9]+$ ]]; then
    HOST="${HOST}:5432"
  fi
  
  export SPRING_DATASOURCE_URL="jdbc:postgresql://${HOST}/${DATABASE}"
  export SPRING_DATASOURCE_USERNAME="${USERNAME}"
  export SPRING_DATASOURCE_PASSWORD="${PASSWORD}"
  
  echo "Database configured from DATABASE_URL"
fi

# Iniciar aplicação na porta 8080
echo "Starting application on port 8080..."
exec java $JAVA_OPTS -jar app.jar --server.port=8080 --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}
