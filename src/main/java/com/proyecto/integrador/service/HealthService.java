package com.proyecto.integrador.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.proyecto.integrador.db.OracleConnectionFactory;

@Service
public class HealthService {
    private final OracleConnectionFactory connectionFactory;

    public HealthService(OracleConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Map<String, Object> testConnection() {
        Map<String, Object> result = new LinkedHashMap<>();
        try (Connection connection = connectionFactory.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            result.put("success", true);
            result.put("message", "Conexión correcta a Oracle");
            result.put("databaseProduct", meta.getDatabaseProductName());
            result.put("databaseVersion", meta.getDatabaseProductVersion());
        } catch (SQLException ex) {
            result.put("success", false);
            result.put("message", ex.getMessage());
        }
        return result;
    }
}
