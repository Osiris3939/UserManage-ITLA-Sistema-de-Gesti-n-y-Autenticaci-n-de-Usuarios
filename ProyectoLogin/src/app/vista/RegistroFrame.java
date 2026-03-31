package app.vista;

import app.modelo.dao.UsuarioDAO;
import app.modelo.entidad.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Pantalla de Registro.
 * Aquí se crean usuarios nuevos con todos los campos obligatorios.
 */
public class RegistroFrame extends JFrame {

    private final LoginFrame loginFrame;

    private final JTextField txtUsername = new JTextField(20);
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtApellido = new JTextField(20);
    private final JTextField txtTelefono = new JTextField(20);
    private final JTextField txtCorreo = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JPasswordField txtConfirmar = new JPasswordField(20);

    private final JCheckBox chkActivo = new JCheckBox("Activo", true);

    private final JButton btnRegistrar = new JButton("Registrar");
    private final JButton btnVolver = new JButton("Volver al login");

    public RegistroFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;

        setTitle("Registro - App");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(520, 430);
        setLocationRelativeTo(null);
        setResizable(false);

        construirUI();
        eventos();
    }

    private void construirUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Registro de usuario");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(lblTitulo, c);
        c.gridwidth = 1;

        agregarFila(panel, c, 1, "Username:", txtUsername);
        agregarFila(panel, c, 2, "Nombre:", txtNombre);
        agregarFila(panel, c, 3, "Apellido:", txtApellido);
        agregarFila(panel, c, 4, "Teléfono:", txtTelefono);
        agregarFila(panel, c, 5, "Correo:", txtCorreo);
        agregarFila(panel, c, 6, "Contraseña:", txtPassword);
        agregarFila(panel, c, 7, "Confirmar:", txtConfirmar);

        c.gridx = 1;
        c.gridy = 8;
        panel.add(chkActivo, c);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.add(btnVolver);
        botones.add(btnRegistrar);

        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        panel.add(botones, c);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnRegistrar);
    }

    private void agregarFila(JPanel panel, GridBagConstraints c, int fila, String label, JComponent campo) {
        c.gridy = fila;
        c.gridx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        panel.add(campo, c);
    }

    private void eventos() {
        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> volver());

        // Si cierran la ventana, lo tratamos como "volver", para no dejar el login escondido.
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                volver();
            }
        });
    }

    private void registrar() {
        String username = txtUsername.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        char[] pass = txtPassword.getPassword();
        char[] conf = txtConfirmar.getPassword();
        boolean activo = chkActivo.isSelected();

        // Aquí validamos que no falte ni un dato para que el registro no de problemas al insertar.
        if (username.isEmpty()) { faltaCampo("Username"); return; }
        if (nombre.isEmpty()) { faltaCampo("Nombre"); return; }
        if (apellido.isEmpty()) { faltaCampo("Apellido"); return; }
        if (telefono.isEmpty()) { faltaCampo("Teléfono"); return; }
        if (correo.isEmpty()) { faltaCampo("Correo"); return; }
        if (pass.length == 0) { faltaCampo("Contraseña"); return; }
        if (conf.length == 0) { faltaCampo("Confirmar"); return; }

        // Validación de confirmación de contraseña (requisito).
        if (!Arrays.equals(pass, conf)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La confirmación de contraseña no coincide.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            Arrays.fill(pass, '\0');
            Arrays.fill(conf, '\0');
            return;
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setTelefono(telefono);
        u.setCorreo(correo);
        // Convertimos la contraseña en el último momento.
        u.setPassword(new String(pass));
        u.setEstado(activo);

        try {
            UsuarioDAO dao = new UsuarioDAO();
            boolean ok = dao.registrar(u);

            if (ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Usuario registrado correctamente. Ahora puede iniciar sesión.",
                        "Registro",
                        JOptionPane.INFORMATION_MESSAGE
                );
                limpiar();
                volver();
            } else {
                // Evitar usuarios duplicados (requisito).
                JOptionPane.showMessageDialog(
                        this,
                        "Ese username ya existe. Intente con otro.",
                        "Registro",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar en la base de datos:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            // Limpiamos la contraseña en memoria.
            Arrays.fill(pass, '\0');
            Arrays.fill(conf, '\0');
        }
    }

    private void faltaCampo(String campo) {
        JOptionPane.showMessageDialog(this, "Falta el campo: " + campo, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    private void volver() {
        dispose();
        loginFrame.volverDesdeRegistro();
    }

    private void limpiar() {
        txtUsername.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        txtConfirmar.setText("");
        chkActivo.setSelected(true);
    }
}
