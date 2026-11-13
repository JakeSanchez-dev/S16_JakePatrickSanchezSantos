package com.empresa.eventos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Punto de entrada del sistema.
 * Esta clase NO extiende Application: solo lanza la aplicacion JavaFX
 * embebida para permitir el empaquetado como JAR ejecutable con maven-shade-plugin.
 */
public final class Launcher {

    private Launcher() {
        // Clase de utilidad / punto de entrada, no instanciable
    }

    public static void main(String[] args) {
        Application.launch(EventoApp.class, args);
    }

    /**
     * Clase interna que extiende Application y configura el escenario principal.
     */
    public static class EventoApp extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/evento_view.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root, 1150, 720);
            scene.getStylesheets().add(
                    getClass().getResource("/styles.css").toExternalForm()
            );

            primaryStage.setTitle("Sistema de Gestion - Empresa de Eventos");
            primaryStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/icono.png"))
            );
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(950);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        }
    }
}
