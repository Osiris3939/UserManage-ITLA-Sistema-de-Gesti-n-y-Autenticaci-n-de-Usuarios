/*
==========================================================
 Sistema de Registro de Usuarios - Proyecto Programación 1
  Franklyn Santana

 Este módulo maneja la creación de la base de datos
 y la tabla principal donde se guardan los usuarios.

 La idea es simular un sistema real de login y registro,
 donde cada usuario tiene sus datos básicos almacenados.

 Nota:
 Todo está hecho de forma sencilla pero funcional,
 pensando en que se pueda escalar más adelante exonereme maestro
 ajajaj.
==========================================================
*/

-- Cambiamos el DROP por un comentario para que no borre nada nunca
-- DROP DATABASE IF EXISTS login_app;

-- Ahora usamos "IF NOT EXISTS" para que no dé error si ya está creada
CREATE DATABASE IF NOT EXISTS login_app;

-- Seleccionando la base para trabajar
USE login_app;

-- También le ponemos "IF NOT EXISTS" a la tabla para proteger tus datos
CREATE TABLE IF NOT EXISTS usuarios (

    id INT AUTO_INCREMENT PRIMARY KEY,
    -- Identificador único del usuario (se genera automáticamente)

    username VARCHAR(50) NOT NULL UNIQUE,
    -- Nombre de usuario, no se puede repetir

    nombre VARCHAR(50) NOT NULL,
    -- Nombre real del usuario

    apellido VARCHAR(50) NOT NULL,
    -- Apellido del usuario

    telefono VARCHAR(20) NOT NULL,
    -- Número de contacto

    correo VARCHAR(100) NOT NULL,
    -- Email del usuario

    password VARCHAR(100) NOT NULL,
    -- Contraseña (se puede encriptar en futuras mejoras)

    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- Fecha en que el usuario se registró automáticamente

    estado BOOLEAN DEFAULT TRUE
    -- Indica si el usuario está activo dentro del sistema
);

-- Tu consulta mágica para ver los resultados:
SELECT * FROM usuarios;

