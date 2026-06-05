#!/bin/bash
set -e

echo "🚀 Inicializando Users Service..."

# Verificar si Docker está disponible
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado."
    exit 1
fi

# Verificar si docker-compose está disponible
if ! command -v docker compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado."
    exit 1
fi

# Crear .env si no existe
if [ ! -f .env ]; then
    echo "📋 Creando .env desde .env.example..."
    cp .env.example .env
    echo "✅ .env creado"
    echo "⚠️  Edita .env si necesitas cambiar algún valor"
else
    echo "✅ .env ya existe"
fi

# Preguntar si compilar
read -p "¿Compilar imagen Docker? (s/n) [s]: " -n 1 -r
echo
if [[ $REPLY =~ ^[Ss]?$ ]]; then
    echo "🔨 Compilando imagen Docker..."
    docker compose build
    echo "✅ Imagen compilada"
fi

# Preguntar si levantar
read -p "¿Levantar stack ahora? (s/n) [s]: " -n 1 -r
echo
if [[ $REPLY =~ ^[Ss]?$ ]]; then
    echo "🏃 Levantando stack..."
    docker compose up -d

    echo "⏳ Esperando a que MySQL esté listo (hasta 30 segundos)..."
    sleep 30

    echo ""
    echo "✅ Stack levantado exitosamente!"
    echo ""
    echo "📍 API disponible en: http://localhost:8080/api/v1/"
    echo "📍 MySQL en: localhost:3306"
    echo ""
    echo "Ver logs: docker compose logs -f"
fi

echo "✅ Inicialización completada"

