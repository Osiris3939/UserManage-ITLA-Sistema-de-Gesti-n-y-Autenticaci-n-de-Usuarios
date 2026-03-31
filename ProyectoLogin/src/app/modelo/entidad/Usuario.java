package app.modelo.entidad;

/**
 * Entidad Usuario.
 * Esto es el "molde" de lo que se guarda en la tabla usuarios.
 *
 * Nota de Prog 1: aquí dejamos solo lo que vamos a usar en la app
 * para que sea más fácil de explicar.
 */
public class Usuario {

    private int id;
    private String username;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String password;
    private boolean estado;

    public Usuario() {
        // Constructor vacío para trabajar cómodo con Swing y el DAO.
    }

    public Usuario(int id, String username, String nombre, String apellido, String telefono, String correo,
                   String password, boolean estado) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.password = password;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}

