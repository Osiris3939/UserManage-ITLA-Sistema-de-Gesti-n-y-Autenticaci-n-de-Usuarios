package app.vista;

import javax.swing.*;

/**
 * Fábrica simple para crear y navegar entre ventanas.
 * La idea es centralizar la navegación para que el código no se riegue en todas las pantallas.
 */
public class VentanaFactory {

    private VentanaFactory() {
        // No se instancia.
    }

    public static void irLogin(JFrame actual) {
        LoginFrame login = new LoginFrame();
        login.setVisible(true);
        if (actual != null) actual.dispose();
    }

    public static void irRegistro(LoginFrame loginActual) {
        RegistroFrame registro = new RegistroFrame(loginActual);
        registro.setVisible(true);
        loginActual.setVisible(false);
    }

    public static void irPrincipal(LoginFrame loginActual) {
        PrincipalFrame principal = new PrincipalFrame(loginActual);
        principal.setVisible(true);
        loginActual.setVisible(false);
    }
}

