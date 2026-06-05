# Setup Rápido

## Con Script (Recomendado)

```bash
./init.sh
```

El script:
1. Verifica que Docker y Docker Compose estén instalados
2. Crea `.env` desde `.env.example`
3. Compila la imagen Docker
4. Levanta el stack
5. Espera a que MySQL esté listo

## Manual

### 1. Copiar configuración

```bash
cp .env.example .env
```

### 2. Compilar imagen

```bash
docker compose build
```

O con Make:

```bash
make docker-build
```

### 3. Levantar stack

```bash
docker compose up -d
```

O con Make:

```bash
make docker-up
```

### 4. Esperar a que MySQL esté listo

```bash
# Esperar ~30 segundos (healthcheck)
sleep 30

# Ver logs
docker compose logs -f app
```

## Verificar que todo funciona

```bash
# Ver contenedores
docker compose ps

# Probar API
curl http://localhost:8080/api/v1/users

# Ver logs
docker compose logs -f
```

## Limpieza

```bash
# Opción 1: Con script
./clean.sh

# Opción 2: Con Make
make docker-down

# Opción 3: Manual
docker compose down -v
```

## Troubleshooting

**MySQL tarda mucho en iniciar**
- Es normal, el healthcheck espera ~30 segundos
- Ver logs: `docker compose logs mysql`

**Puerto 8080 en uso**
- Edita `.env` y cambia `PORT=9090`
- Levanta de nuevo: `docker compose up -d`

**Error "Address already in use"**
- Ejecuta: `docker compose down` primero
- Luego: `docker compose up -d`

**Ver logs en tiempo real**
- `docker compose logs -f`
- `docker compose logs -f app`
- `docker compose logs -f mysql`

