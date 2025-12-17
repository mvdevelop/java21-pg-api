
#!/bin/bash

# Configurações
IMAGE_NAME="api-java-app"
CONTAINER_NAME="api-java-container"
PORT_HOST=8080
PORT_CONTAINER=8081
VERSION="latest"

# Parar se ocorrer erro
set -e

# Função para help
show_help() {
    echo "Uso: $0 [comando]"
    echo ""
    echo "Comandos:"
    echo "  build     - Apenas construir a imagem"
    echo "  run       - Apenas executar o container (presume imagem existente)"
    echo "  build-run - Construir e executar (padrão)"
    echo "  logs      - Mostrar logs do container"
    echo "  stop      - Parar container"
    echo "  clean     - Remover container e imagem"
    echo "  help      - Mostrar esta ajuda"
}

# Comando padrão
COMMAND=${1:-"build-run"}

case $COMMAND in
    "build")
        echo "🔨 Construindo imagem $IMAGE_NAME:$VERSION..."
        docker build -t $IMAGE_NAME:$VERSION .
        echo "✅ Imagem construída!"
        ;;
        
    "run")
        echo "🚀 Iniciando container..."
        docker run -d \
            --name $CONTAINER_NAME \
            -p $PORT_HOST:$PORT_CONTAINER \
            --restart unless-stopped \
            $IMAGE_NAME:$VERSION
        echo "✅ Container $CONTAINER_NAME iniciado na porta $PORT_HOST"
        ;;
        
    "build-run")
        echo "🔨 Construindo imagem..."
        docker build -t $IMAGE_NAME:$VERSION .
        
        echo "🧹 Removendo container anterior..."
        docker rm -f $CONTAINER_NAME 2>/dev/null || true
        
        echo "🚀 Iniciando novo container..."
        docker run -d \
            --name $CONTAINER_NAME \
            -p $PORT_HOST:$PORT_CONTAINER \
            --restart unless-stopped \
            $IMAGE_NAME:$VERSION
            
        echo "✅ Pronto! Acesse: http://localhost:$PORT_HOST"
        echo "📋 Logs: docker logs -f $CONTAINER_NAME"
        ;;
        
    "logs")
        docker logs -f $CONTAINER_NAME
        ;;
        
    "stop")
        echo "🛑 Parando container..."
        docker stop $CONTAINER_NAME
        echo "✅ Container parado"
        ;;
        
    "clean")
        echo "🧼 Limpando tudo..."
        docker stop $CONTAINER_NAME 2>/dev/null || true
        docker rm $CONTAINER_NAME 2>/dev/null || true
        docker rmi $IMAGE_NAME:$VERSION 2>/dev/null || true
        echo "✅ Limpeza concluída"
        ;;
        
    "help"|"-h"|"--help")
        show_help
        ;;
        
    *)
        echo "❌ Comando desconhecido: $COMMAND"
        show_help
        exit 1
        ;;
esac