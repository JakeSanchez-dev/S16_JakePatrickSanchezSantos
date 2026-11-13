package com.empresa.eventos.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Utilitario para acciones de navegacion y apertura de archivos externos
 * (por ejemplo, abrir el PDF generado por JasperReports).
 */
public final class NavigationUtil {

    private NavigationUtil() {
        // Clase de utilidad, no instanciable
    }

    /**
     * Abre un archivo con la aplicacion predeterminada del sistema operativo.
     *
     * @param archivo archivo a abrir
     * @return true si se abrio correctamente, false en caso contrario
     */
    public static boolean abrirArchivo(File archivo) {
        if (archivo == null || !archivo.exists()) {
            return false;
        }
        try {
            Desktop.getDesktop().open(archivo);
            return true;
        } catch (IOException e) {
            System.err.println("No se pudo abrir el archivo: " + e.getMessage());
            return false;
        }
    }
}
