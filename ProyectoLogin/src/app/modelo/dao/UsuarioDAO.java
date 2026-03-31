package app.modelo.dao;

import app.modelo.dao.util.ConexionBD;
import app.modelo.entidad.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DAO de Usuario.
 * Aquí van las consultas SQL de la tabla usuarios.
 *
 * Nota lo dejamos directo, sin lógica rara, para que sea fácil de explicar.
 */
public class UsuarioDAO {

    /**
     * Login simple:
     * busca el username y compara la contraseña tal cual está en la base de datos.
     *
     * Importante: para una tarea real se recomienda encriptar/hashear,
     * pero aquí se trabaja simple para fines académicos.
     */
    public boolean login(String username, char[] password) throws SQLException {
        String sql = "SELECT password, estado FROM usuarios WHERE username = ? LIMIT 1";

        // Convertimos lo más tarde posible para no manejar la contraseña como texto por mucho tiempo.
        String passIngresada = new String(password);

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;

                String passBD = rs.getString("password"); // en este proyecto se guarda en texto plano
                boolean estado = rs.getBoolean("estado"); // si está desactivado, no deja entrar

                return estado && passIngresada.equals(passBD);
            }
        } finally {
            // Limpiamos el arreglo para no dejar la contraseña en memoria.
            Arrays.fill(password, '\0');
        }
    }

    /**
     * Registra un usuario nuevo.
     * Devuelve true si insertó, false si no se insertó por alguna razón.
     *
     * Importante: aquí se evita duplicar username, pero igual la BD tiene UNIQUE,
     * o sea, es doble seguridad.
     */
    public boolean registrar(Usuario u) throws SQLException {
        if (existeUsername(u.getUsername())) {
            return false;
        }

        String sql = "INSERT INTO usuarios (username, nombre, apellido, telefono, correo, password, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getCorreo());
            ps.setString(6, u.getPassword()); // texto plano (simplificado)
            ps.setBoolean(7, u.isEstado());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lista usuarios (opcional, pero sencillo).
     * Esto sirve para demostrar que también podemos "leer" desde MySQL.
     */
    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id, username, nombre, apellido, telefono, correo, estado " +
                "FROM usuarios ORDER BY id";

        List<Usuario> lista = new ArrayList<>();

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setTelefono(rs.getString("telefono"));
                u.setCorreo(rs.getString("correo"));
                u.setEstado(rs.getBoolean("estado"));
                lista.add(u);
            }
        }

        return lista;
    }

    /**
     * Actualiza los datos del usuario por ID.
     * Esto se usa cuando se presiona el botón de actualizar en el panel principal.
     */
    public boolean actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET username=?, nombre=?, apellido=?, telefono=?, correo=? WHERE id=?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getCorreo());
            ps.setInt(6, u.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Método para sacar a un usuario de la base de datos cuando se presiona el botón de eliminar.
     */
    public boolean eliminarPorId(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id=?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Verifica si existe un username.
     */
    public boolean existeUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE username = ? LIMIT 1";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

