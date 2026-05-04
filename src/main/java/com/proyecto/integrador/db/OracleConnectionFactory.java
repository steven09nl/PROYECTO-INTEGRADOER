package com.proyecto.integrador.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OracleConnectionFactory {

    @Value("${app.oracle.url}")
    private String url;

    @Value("${app.oracle.user}")
    private String user;

    @Value("${app.oracle.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontró el driver de Oracle JDBC", ex);
        }
        return DriverManager.getConnection(url, user, password);
    }
}
