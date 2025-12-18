
#!/bin/sh

# Configurações para Render
export JAVA_OPTS="${JAVA_OPTS} -Dspring.main.lazy-initialization=true -Dspring.main.web-application-type=servlet"

# Iniciar aplicação em background
echo "🚀 Iniciando aplicação Spring Boot..."
java $JAVA_OPTS -jar app.jar --server.port=8080 --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} &

# Guardar PID
APP_PID=$!

# Aguardar startup
echo "⏳ Aguardando aplicação iniciar (máx 90s)..."

# Tentar health check por até 90 segundos
for i in $(seq 1 90); do
    if curl -s -f http://localhost:8080/health > /dev/null 2>&1; then
        echo "✅ Aplicação iniciada e respondendo!"
        echo "📡 Disponível em: http://localhost:8080"
        echo "🔧 PID: $APP_PID"
        
        # Manter container rodando
        wait $APP_PID
        exit $?
    fi
    
    # Verificar se processo ainda está vivo
    if ! kill -0 $APP_PID 2>/dev/null; then
        echo "❌ Processo da aplicação morreu"
        exit 1
    fi
    
    sleep 1
done

echo "❌ Timeout: Aplicação não respondeu após 90 segundos"
kill $APP_PID
exit 1
