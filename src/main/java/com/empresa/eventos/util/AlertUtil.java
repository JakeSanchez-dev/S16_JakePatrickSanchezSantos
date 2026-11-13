package com.empresa.eventos.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utilitario para mostrar alertas visuales estandarizadas en la interfaz.
 */
public final class AlertUtil {

    private AlertUtil() {
        // Clase de utilidad, no instanciable
    }

    public static void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        return alert.showAndWait().filter(ButtonType.YES::equals).isPresent();
    }
}
