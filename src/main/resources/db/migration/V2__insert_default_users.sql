-- V2: Datos semilla (usuarios por defecto para pruebas).
--
-- Las contraseñas están cifradas con BCrypt (igual que en producción).
-- Credenciales en texto plano para que los estudiantes puedan iniciar sesión:
--   admin@kiosco.cl / admin123   (rol ADMIN - acceso total)
--   user@kiosco.cl  / user123    (rol USER  - solo lectura de /users)
--
-- Nota: NO se usa prefijo de base de datos (ej. `kiosco_users`.`users`)
-- para que el script funcione en cualquier esquema (MySQL, H2 en tests, etc.).
INSERT INTO users (active, email, password, role) VALUES
    (b'1', 'admin@kiosco.cl', '$2b$12$x7uSsHJfg8oyv.JZhEMnz.7PQTAiaq09.YujzJY4MRBsvMtHigOlu', 'ADMIN'),
    (b'1', 'user@kiosco.cl',  '$2b$12$Kj0P6S2vrw3aR6cW0fbts..vbjS6GkI/tvacadhjtJUQjpn5iN0RS', 'USER');
