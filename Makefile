.PHONY: help build start stop logs test clean docker-build docker-up docker-down docker-logs

help:
	@echo "Comandos disponibles para el proyecto Users Service:"
	@echo ""
	@echo "Desarrollo local:"
	@echo "  make test              - Ejecutar pruebas unitarias"
	@echo "  make build             - Compilar el proyecto"
	@echo "  make clean             - Limpiar archivos compilados"
	@echo ""
	@echo "Docker (desarrollo):"
	@echo "  make docker-build      - Compilar imagen Docker"
	@echo "  make docker-up         - Levantar stack (MySQL + API)"
	@echo "  make docker-down       - Detener y eliminar contenedores"
	@echo "  make docker-logs       - Ver logs de los contenedores"
	@echo ""
	@echo "Docker (producción):"
	@echo "  make docker-prod-up    - Levantar stack con perfil de producción"
	@echo "  make docker-prod-down  - Detener stack de producción"
	@echo ""

# Targets locales
test:
	@echo "Ejecutando pruebas..."
	./mvnw -q test

build:
	@echo "Compilando proyecto..."
	./mvnw -q clean package -DskipTests

clean:
	@echo "Limpiando archivos compilados..."
	./mvnw -q clean

# Targets Docker (desarrollo)
docker-build:
	@echo "Compilando imagen Docker..."
	docker compose build

docker-up:
	@echo "Levantando stack de desarrollo..."
	docker compose up -d
	@echo "API disponible en: http://localhost:8080/api/v1/"
	@echo "MySQL disponible en: localhost:3306"

docker-down:
	@echo "Deteniendo stack..."
	docker compose down

docker-logs:
	@echo "Mostrando logs (presiona Ctrl+C para salir)..."
	docker compose logs -f

docker-restart:
	@echo "Reiniciando containers..."
	docker compose restart

# Targets Docker (producción)
docker-prod-up:
	@echo "Levantando stack de PRODUCCIÓN..."
	@if [ ! -f .env ]; then \
		echo "ERROR: .env no existe. Copia .env.example a .env y configura las variables."; \
		exit 1; \
	fi
	docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d
	@echo "API disponible en: http://localhost:8080/api/v1/"

docker-prod-down:
	@echo "Deteniendo stack de producción..."
	docker compose -f docker-compose.yml -f docker-compose.prod.yml down

docker-prod-logs:
	@echo "Mostrando logs de producción..."
	docker compose -f docker-compose.yml -f docker-compose.prod.yml logs -f

# Utilidades
docker-ps:
	@echo "Contenedores activos:"
	docker compose ps

docker-shell-app:
	@echo "Entrando al shell del contenedor app..."
	docker compose exec app /bin/sh

docker-shell-mysql:
	@echo "Entrando a MySQL..."
	docker compose exec mysql mysql -u root -proot -D users

