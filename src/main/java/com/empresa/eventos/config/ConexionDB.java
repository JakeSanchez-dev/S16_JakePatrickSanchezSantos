package com.empresa.eventos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexion a la base de datos MySQL.
 * Utiliza JDBC puro. Las credenciales se configuran aqui como constantes.
 */
public final class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3308/db_empresa_eventos"
                    + "?useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=America/Lima";
    private static final String USUARIO = "admin_eventos";
    private static final String CONTRASENA = "eventos123";

    private ConexionDB() {
        // Constructor privado para evitar instanciacion directa
    }

    /**
     * Obtiene una nueva conexion a la base de datos MySQL.
     *
     * @return objeto {@link Connection} abierto
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    /**
     * Cierra una conexion de forma segura, ignorando errores de cierre.
     *
     * @param conn conexion a cerrar (puede ser null)
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
}