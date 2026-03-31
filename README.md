# UserManage-ITLA: Sistema de Gestión de Usuarios

Este proyecto es una aplicación de escritorio desarrollada en **Java** para la materia de **Programación 1** en el **ITLA**. El sistema permite gestionar el ciclo de vida completo de los usuarios (CRUD), integrando una interfaz gráfica intuitiva con persistencia de datos en MySQL.

## 👤 Información del Desarrollador
* **Nombre:** Franklyn Enmanuel Santana Rodriguez
* **Matrícula:** [20252089]
* **Institución:** Instituto Tecnológico de las Américas (ITLA)

---

## 🚀 Características del Sistema
Siguiendo estrictamente el mandato de la Tarea 4, el sistema incluye:

* **Autenticación Segura:** Login con ocultamiento de contraseña y validación de campos vacíos.
* **Registro de Usuarios:** Formulario con 7 campos obligatorios y validación de coincidencia de contraseñas.
* **Gestión CRUD:** Panel principal con `JTable` para visualizar, actualizar y eliminar registros en tiempo real.
* **Navegación Fluida:** Botón de cierre de sesión que garantiza el retorno seguro al Login.

---

## 🛠️ Arquitectura y Patrones de Diseño
Para este proyecto se aplicaron estándares de industria para asegurar un código escalable y limpio:

1. **Patrón Singleton:** Implementado en la clase `ConexionBD` para manejar una única instancia de conexión a la base de datos, optimizando el uso de recursos.
2. **Patrón Factory:** Utilizado para la creación y gestión de ventanas (`Frames`), centralizando la navegación de la interfaz.
3. **Pilares POO:** Uso extensivo de **Encapsulamiento** (clases entidad), **Herencia** (vistas Swing) y **Abstracción**.

---

## ⚙️ Requisitos e Instalación

### Requisitos Previos
* **Java SDK** (Versión 17 o superior recomendada).
* **MySQL Server** (Puerto 3306).
* **MySQL Connector J** (Incluido en la carpeta `/lib`).

### Configuración
1. **Base de Datos:** Ejecute el script localizado en `database.sql` para crear la base de datos `login_app` y las tablas necesarias.
2. **Conexión:** La configuración por defecto utiliza el usuario `root` y la clave `Franklyn2727`.
3. **Ejecución:** Compile y ejecute la clase principal desde su IDE preferido (Cursor, VS Code o NetBeans).

---

## 📜 Licencia
Este proyecto es de carácter académico para el ITLA. Todos los derechos reservados © 2026.
