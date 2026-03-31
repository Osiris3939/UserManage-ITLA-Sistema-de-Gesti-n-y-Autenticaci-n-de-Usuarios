package app.vista;

import app.modelo.dao.UsuarioDAO;
import app.modelo.entidad.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

/**
 * Pantalla de Login.
 * Aquí el usuario entra con username y password.
 *
 * Nota de Prog 1: lo dejamos sencillo, sin más pantallas, para que sea fácil de exponer.
 */
public class LoginFrame extends JFrame {

    private final JTextField txtUsuario = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Iniciar sesión");
    private final JButton btnIrRegistro = new JButton("Registrarme");
    private final JButton btnVerUsuarios = new JButton("Ver usuarios (opcional)");

    public LoginFrame() {
        setTitle("Login - App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        construirUI();
        eventos();
    }

    private void construirUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(lblTitulo, c);

        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 0;
        panel.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        panel.add(txtUsuario, c);

        c.gridy = 2;
        c.gridx = 0;
        panel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        panel.add(txtPassword, c);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.add(btnIrRegistro);
        botones.add(btnVerUsuarios);
        botones.add(btnLogin);

        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(botones, c);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void eventos() {
        btnLogin.addActionListener(e -> intentarLogin());
        btnIrRegistro.addActionListener(e -> {
            VentanaFactory.irRegistro(this);
        });
        btnVerUsuarios.addActionListener(e -> mostrarUsuarios());
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        char[] pass = txtPassword.getPassword();

        // Validación de campos vacíos (requisito).
        if (usuario.isEmpty() || pass.length == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "debe ingresar su usuario y contraseña, si no está registrado debe registrarse",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            boolean ok = dao.login(usuario, pass);

            if (ok) {
                limpiarCampos();
                // Al entrar con éxito, debe abrir el PrincipalFrame.
                VentanaFactory.irPrincipal(this);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Usuario o contraseña incorrectos, o el usuario está desactivado.",
                        "Login",
                        JOptionPane.ERROR_MESSAGE
                );
                Arrays.fill(pass, '\0');
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al conectar o consultar la base de datos:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            Arrays.fill(pass, '\0');
        }
    }

    private void mostrarUsuarios() {
        // Esto es opcional. Sirve para demostrar el SELECT de manera simple.
        try {
            UsuarioDAO dao = new UsuarioDAO();
            List<Usuario> usuarios = dao.listar();

            if (usuarios.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay usuarios registrados todavía.", "Usuarios",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (Usuario u : usuarios) {
                sb.append(u.getId())
                        .append(" - ")
                        .append(u.getUsername())
                        .append(" (")
                        .append(u.getNombre())
                        .append(" ")
                        .append(u.getApellido())
                        .append(")")
                        .append(u.isEstado() ? " [activo]" : " [inactivo]")
                        .append("\n");
            }

            JTextArea area = new JTextArea(sb.toString(), 12, 32);
            area.setEditable(false);
            area.setCaretPosition(0);
            JScrollPane scroll = new JScrollPane(area);

            JOptionPane.showMessageDialog(this, scroll, "Lista de usuarios", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar usuarios:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void volverDesdeRegistro() {
        // Esto lo llama RegistroFrame cuando el usuario vuelve.
        setVisible(true);
        txtUsuario.requestFocus();
    }

    private void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
    }
}

