package app.modelo.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Maneja la conexión a MySQL con JDBC.
 * La idea es centralizar aquí la configuración para que el DAO no tenga que repetirla.
 *
 * Usamos el Singleton para no estar abriendo conexiones a lo loco y ahorrar recursos del sistema.
 */
public class ConexionBD {

    // Datos de conexión exactos del proyecto.
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/login_app";
    private static final String USUARIO = "root";
    private static final String CLAVE = "Franklyn2727";

    // Instancia única de la clase (Singleton).
    private static ConexionBD instancia;

    // Conexión única que se reutiliza en los DAOs.
    private Connection conexion;

    private ConexionBD() throws SQLException {
        // Cargamos el driver y abrimos la conexión una sola vez.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL. Verifica el .jar en el classpath.", e);
        }
        this.conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    /**
     * Devuelve la instancia única de la clase.
     */
    public static synchronized ConexionBD getInstancia() throws SQLException {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Devuelve la conexión ya abierta.
     * Si por alguna razón la conexión se cerró, se vuelve a abrir.
     */
    public synchronized Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
        }
        return conexion;
    }
}

