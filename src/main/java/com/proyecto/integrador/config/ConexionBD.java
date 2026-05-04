package com.proyecto.integrador.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // ============================
    // CONEXIÓN UNIVERSIDAD
    // ============================
    private static final String URL_UNI = "jdbc:oracle:thin:@192.168.254.215:1521:orcl";

    // ============================
    // CONEXIÓN LOCALHOST
    // ============================
    private static final String URL_LOCAL = "jdbc:oracle:thin:@localhost:1521:XE";

    // 🔥 CAMBIA AQUÍ LA URL ACTIVA:
    private static final String URL = URL_UNI;
    // private static final String URL = URL_LOCAL;

    private static final String USUARIO = "practicas";
    private static final String CLAVE = "practicas";

    private static Connection conexion;

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("oracle.jdbc.OracleDriver");
                conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
                System.out.println("Conexión Oracle establecida con éxito: " + URL);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver Oracle JDBC.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con Oracle: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión Oracle cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}