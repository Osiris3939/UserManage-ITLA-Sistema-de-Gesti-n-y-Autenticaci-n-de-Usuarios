package app.vista;

import javax.swing.*;

/**
 * Punto de entrada del programa.
 * Se ejecuta desde aquí.
 */
public class Main {

    public static void main(String[] args) {
        // Para que se vea mejor en Windows, intentamos usar el look and feel del sistema.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Si falla, no pasa nada, Swing usa el look por defecto.
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}

