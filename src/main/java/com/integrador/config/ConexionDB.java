package com.integrador.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    // Conexión Universidad
    private static final String URL_UNIVERSIDAD = "jdbc:oracle:thin:@192.168.254.215:1521:orcl";
    // Conexión Localhost para pruebas
    private static final String URL_LOCAL = "jdbc:oracle:thin:@localhost:1521:xe"; // o orcl dependiendo de tu SID local
    
    // Cambia esta variable a URL_LOCAL cuando vayas a probar en tu máquina
    private static final String URL_ACTUAL = URL_LOCAL;
    
    // Credenciales proporcionadas
    private static final String USUARIO = "practicas";
    private static final String PASSWORD = "practicas";

    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver de Oracle
            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection(URL_ACTUAL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de Oracle no encontrado.");
            throw new SQLException(e);
        }
    }
}