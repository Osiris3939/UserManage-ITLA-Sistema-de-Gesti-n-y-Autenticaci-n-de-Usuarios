package app.vista;

import app.modelo.dao.UsuarioDAO;
import app.modelo.entidad.Usuario;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel principal del sistema.
 * Aquí se cargan los usuarios en una tabla y se permite actualizar y eliminar.
 */
public class PrincipalFrame extends JFrame {

    private final LoginFrame loginFrame;
    private final UsuarioDAO dao = new UsuarioDAO();

    private final JTable tabla = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Apellido", "Teléfono", "Correo", "Usuario"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTextField txtId = new JTextField(6);
    private final JTextField txtNombre = new JTextField(16);
    private final JTextField txtApellido = new JTextField(16);
    private final JTextField txtTelefono = new JTextField(16);
    private final JTextField txtCorreo = new JTextField(16);
    private final JTextField txtUsuario = new JTextField(16);

    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnRefrescar = new JButton("Refrescar");
    private final JButton btnCerrarSesion = new JButton("Cerrar Sección");

    public PrincipalFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;

        setTitle("Principal - Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        construirUI();
        eventos();
        cargarTabla();
    }

    private void construirUI() {
        txtId.setEnabled(false);

        tabla.setModel(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del usuario seleccionado"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("ID:"), c);
        c.gridx = 1;
        form.add(txtId, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Nombre:"), c);
        c.gridx = 1;
        form.add(txtNombre, c);

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Apellido:"), c);
        c.gridx = 1;
        form.add(txtApellido, c);

        c.gridx = 2; c.gridy = 0;
        form.add(new JLabel("Teléfono:"), c);
        c.gridx = 3;
        form.add(txtTelefono, c);

        c.gridx = 2; c.gridy = 1;
        form.add(new JLabel("Correo:"), c);
        c.gridx = 3;
        form.add(txtCorreo, c);

        c.gridx = 2; c.gridy = 2;
        form.add(new JLabel("Usuario:"), c);
        c.gridx = 3;
        form.add(txtUsuario, c);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.add(btnRefrescar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnCerrarSesion);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Usuarios"));

        JPanel arriba = new JPanel(new BorderLayout(10, 10));
        arriba.add(form, BorderLayout.CENTER);
        arriba.add(botones, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, arriba, scroll);
        split.setResizeWeight(0.45);
        split.setDividerLocation(220);
        setContentPane(split);
    }

    private void eventos() {
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnActualizar.addActionListener(e -> actualizarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        tabla.getSelectionModel().addListSelectionListener(this::seleccionarFila);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarSesion();
            }
        });
    }

    private void cargarTabla() {
        try {
            List<Usuario> usuarios = dao.listar();
            modelo.setRowCount(0);

            for (Usuario u : usuarios) {
                modelo.addRow(new Object[]{
                        u.getId(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getTelefono(),
                        u.getCorreo(),
                        u.getUsername()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarFila(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        txtId.setText(String.valueOf(modelo.getValueAt(fila, 0)));
        txtNombre.setText(String.valueOf(modelo.getValueAt(fila, 1)));
        txtApellido.setText(String.valueOf(modelo.getValueAt(fila, 2)));
        txtTelefono.setText(String.valueOf(modelo.getValueAt(fila, 3)));
        txtCorreo.setText(String.valueOf(modelo.getValueAt(fila, 4)));
        txtUsuario.setText(String.valueOf(modelo.getValueAt(fila, 5)));
    }

    private void actualizarSeleccionado() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario en la tabla.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setId(Integer.parseInt(txtId.getText().trim()));
        u.setNombre(txtNombre.getText().trim());
        u.setApellido(txtApellido.getText().trim());
        u.setTelefono(txtTelefono.getText().trim());
        u.setCorreo(txtCorreo.getText().trim());
        u.setUsername(txtUsuario.getText().trim());

        // Aquí validamos que no falte ni un dato para que la actualización no de problemas.
        if (u.getNombre().isEmpty() || u.getApellido().isEmpty() || u.getTelefono().isEmpty()
                || u.getCorreo().isEmpty() || u.getUsername().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos antes de actualizar.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (dao.actualizar(u)) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Actualizar",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario.", "Actualizar",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar:\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario en la tabla.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int op = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este usuario?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            if (dao.eliminarPorId(id)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.", "Eliminar",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.", "Eliminar",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar:\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtUsuario.setText("");
        tabla.clearSelection();
    }

    private void cerrarSesion() {
        // Cierra el panel y vuelve al Login.
        dispose();
        loginFrame.setVisible(true);
    }
}

