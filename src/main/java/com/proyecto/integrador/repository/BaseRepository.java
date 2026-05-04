package com.proyecto.integrador.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.exception.DaoException;

public abstract class BaseRepository {
    protected final OracleConnectionFactory connectionFactory;

    protected BaseRepository(OracleConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    protected <T> T withConnection(SqlFunction<Connection, T> action) {
        try (Connection connection = connectionFactory.getConnection()) {
            return action.apply(connection);
        } catch (SQLException ex) {
            throw new DaoException("Error de base de datos", ex);
        }
    }

    protected void withConnectionVoid(SqlConsumer<Connection> action) {
        try (Connection connection = connectionFactory.getConnection()) {
            action.accept(connection);
        } catch (SQLException ex) {
            throw new DaoException("Error de base de datos", ex);
        }
    }

    protected long currval(Connection connection, String sequenceName) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT " + sequenceName + ".CURRVAL FROM dual");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("No se pudo obtener CURRVAL de " + sequenceName);
        }
    }

    @FunctionalInterface
    protected interface SqlFunction<T, R> {
        R apply(T value) throws SQLException;
    }

    @FunctionalInterface
    protected interface SqlConsumer<T> {
        void accept(T value) throws SQLException;
    }
}
