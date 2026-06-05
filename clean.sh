#!/bin/bash
set -e

echo "🗑️  Limpiando Users Service..."

# Preguntar confirmación
read -p "¿Realmente deseas eliminar los contenedores y volúmenes? (s/n) [n]: " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Ss]$ ]]; then
    echo "❌ Cancelado"
    exit 1
fi

echo "🛑 Deteniendo y eliminando stack..."
docker compose down -v

echo "🧹 Limpiando imágenes locales (opcional)..."
read -p "¿Eliminar imagen local 'demo-users-app'? (s/n) [n]: " -n 1 -r
echo
if [[ $REPLY =~ ^[Ss]$ ]]; then
    docker rmi -f demo-users-app 2>/dev/null || true
fi

echo "✅ Limpieza completada"

