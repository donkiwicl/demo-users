# Users Service

Servicio Spring Boot para gestión de usuarios, preparado para ejecutarse en Docker con MySQL.

## Requisitos

- Docker y Docker Compose
- Java 21 y Maven si quieres ejecutar fuera de Docker
- Make (opcional, para usar los comandos del Makefile)

## Inicio Rápido (Desarrollo)

```bash
# 1. Copiar configuración local
cp .env.example .env

# 2. Opción A: Con Make
make docker-up
make docker-logs

# 2. Opción B: Sin Make
docker compose up --build
```

La API quedará disponible en:

```text
http://localhost:8080/api/v1/
```

## Uso del Makefile

Comandos disponibles:

```bash
# Mostrar ayuda
make help

# Pruebas y compilación local
make test              # Ejecutar pruebas
make build             # Compilar el proyecto
make clean             # Limpiar archivos compilados

# Docker desarrollo
make docker-build      # Compilar imagen Docker
make docker-up         # Levantar stack
make docker-down       # Detener stack
make docker-logs       # Ver logs
make docker-restart    # Reiniciar contenedores
make docker-ps         # Ver contenedores activos

# Docker producción
make docker-prod-up    # Levantar en producción
make docker-prod-down  # Detener producción
make docker-prod-logs  # Ver logs de producción

# Acceso a shells
make docker-shell-app     # Shell del contenedor app
make docker-shell-mysql   # Shell de MySQL
```

## Variables de Entorno

La aplicación espera estas variables en `application.properties` o como variables de entorno:

- `PORT` - Puerto del servidor (default: 8080)
- `MYSQL_HOST` - Host de MySQL (default: mysql)
- `MYSQL_PORT` - Puerto de MySQL (default: 3306)
- `MYSQL_DATABASE` - Nombre de la base de datos
- `MYSQL_USER` - Usuario de MySQL
- `MYSQL_PASSWORD` - Contraseña de MySQL
- `MYSQL_ROOT_PASSWORD` - Contraseña root de MySQL (solo Docker)

### Desarrollo Local

Usa `.env.example` como plantilla:

```bash
cp .env.example .env
```

### Producción

Usa `.env.prod` como referencia, pero **define todas las variables explícitamente**:

```bash
cp .env.prod .env.prod.local
# Edita .env.prod.local con valores reales
docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d --env-file .env.prod.local
```

## Pruebas Locales

```bash
# Con Maven
./mvnw test

# O con Make
make test
```

Las pruebas usan H2 en memoria, sin depender de MySQL externo.

## Estructura de Docker

### docker-compose.yml (base)
Configuración común para todos los entornos.

### docker-compose.override.yml (desarrollo)
Se carga automáticamente en desarrollo local. Define puertos expostos y ajustes de desarrollo.

### docker-compose.prod.yml (producción)
Se usa explícitamente: `docker compose -f docker-compose.yml -f docker-compose.prod.yml`
Define límites de recursos, politicas de reinicio y volúmenes persistentes.

## Compilación y Despliegue

### Build local

```bash
make build
```

### Build Docker

```bash
make docker-build
```

### Ver logs en tiempo real

```bash
make docker-logs
```

### Acceder a MySQL

```bash
make docker-shell-mysql
```

Usa credenciales del `.env`:
- Usuario: `users`
- Contraseña: (definida en `MYSQL_PASSWORD`)
- Base de datos: `users`

## Notas de Producción

- Los valores de `MYSQL_ROOT_PASSWORD`, `MYSQL_USER`, y `MYSQL_PASSWORD` **deben ser diferentes** en producción
- Usa un `.env.prod.local` (no versionado) para credenciales reales
- Configura límites de CPU y memoria según tu infra
- El volumen `mysql_prod_data` persiste los datos entre reinicios
- Monitorea logs y usa herramientas como Prometheus/Grafana si es necesario

## Limpieza

```bash
# Detener y eliminar todo
make docker-down

# Incluye volúmenes (CUIDADO: elimina datos)
docker compose down -v
```

## Troubleshooting

- **"port 8080 is already in use"**: Cambia `PORT` en `.env` o detén otros servicios
- **"MySQL connection refused"**: Espera a que MySQL esté listo (healthcheck tardará ~30s)
- **"Address already in use"**: `docker compose down` antes de `docker compose up --build`
