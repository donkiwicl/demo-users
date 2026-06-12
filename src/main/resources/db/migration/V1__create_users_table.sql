-- V1: Creación de la tabla de usuarios.
-- Incluye todas las columnas del modelo (id, email, password, active, role)
-- para que el esquema coincida con la entidad User y supere la validación de
-- Hibernate (spring.jpa.hibernate.ddl-auto=validate).
CREATE TABLE users (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    email    VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    active   BIT          NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE = InnoDB;
