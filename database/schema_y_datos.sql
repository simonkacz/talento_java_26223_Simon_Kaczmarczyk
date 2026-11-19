-- Script SQL para XAMPP (MySQL / MariaDB)
-- Proyecto: CRUD de Artículos (Entrega Final - Spring Boot + JPA + MySQL)

CREATE DATABASE IF NOT EXISTS articulos_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE articulos_db;

-- Tabla de Categorías
CREATE TABLE IF NOT EXISTS categorias (
    codigo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- Tabla única de Artículos (Estrategia SINGLE_TABLE para herencia POO)
CREATE TABLE IF NOT EXISTS articulos (
    codigo BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_articulo VARCHAR(31) NOT NULL DEFAULT 'GENERAL',
    nombre VARCHAR(120) NOT NULL,
    precio DOUBLE NOT NULL,
    categoria_codigo BIGINT NULL,
    garantia_meses INT NULL,
    dias_para_vencimiento INT NULL,
    CONSTRAINT fk_articulo_categoria
        FOREIGN KEY (categoria_codigo) REFERENCES categorias(codigo)
        ON DELETE SET NULL
);

-- Datos iniciales de prueba (ejecutar solo si las tablas están vacías)
INSERT INTO categorias (nombre, descripcion) VALUES
    ('Electrónica', 'Dispositivos tecnológicos'),
    ('Alimentos', 'Comestibles y bebidas'),
    ('Librería y Oficina', 'Insumos escolares y de escritorio');

INSERT INTO articulos (tipo_articulo, nombre, precio, categoria_codigo, garantia_meses, dias_para_vencimiento) VALUES
    ('ELECTRONICO', 'Teclado Mecánico', 45000.0, 1, 12, NULL),
    ('ALIMENTICIO', 'Leche Entera', 1200.0, 2, NULL, 5),
    ('GENERAL', 'Cuaderno A4', 3500.0, 3, NULL, NULL);
