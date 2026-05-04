package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.Bitacora;

@Repository
public class BitacoraRepository extends BaseRepository {
    public BitacoraRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public Long save(Bitacora b) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Bitacora (id_estudiante, id_practica, estado, modalidad, fecha_envio, calificacion) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, b.getIdEstudiante());
                ps.setLong(2, b.getIdPractica());
                ps.setString(3, b.getEstado());
                ps.setString(4, b.getModalidad());
                if (b.getFechaEnvio() == null) ps.setNull(5, Types.DATE); else ps.setDate(5, Date.valueOf(b.getFechaEnvio()));
                if (b.getCalificacion() == null) ps.setNull(6, Types.NUMERIC); else ps.setDouble(6, b.getCalificacion());
                ps.executeUpdate();
                return currval(connection, "seq_bitacora");
            }
        });
    }

    public Bitacora findById(Long id) {
        return withConnection(connection -> {
            String sql = "SELECT id_bitacora, id_estudiante, id_practica, estado, modalidad, fecha_envio, calificacion FROM Bitacora WHERE id_bitacora = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? map(rs) : null;
                }
            }
        });
    }

    public List<Bitacora> findByEstudiante(Long idEstudiante) {
        return withConnection(connection -> {
            List<Bitacora> list = new ArrayList<>();
            String sql = "SELECT id_bitacora, id_estudiante, id_practica, estado, modalidad, fecha_envio, calificacion FROM Bitacora WHERE id_estudiante = ? ORDER BY id_bitacora DESC";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    public List<Bitacora> findByPractica(Long idPractica) {
        return withConnection(connection -> {
            List<Bitacora> list = new ArrayList<>();
            String sql = "SELECT id_bitacora, id_estudiante, id_practica, estado, modalidad, fecha_envio, calificacion FROM Bitacora WHERE id_practica = ? ORDER BY id_bitacora DESC";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idPractica);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    public boolean updateStateAndGrade(Long idBitacora, String estado, Double calificacion) {
        return withConnection(connection -> {
            String sql = "UPDATE Bitacora SET estado = ?, calificacion = ? WHERE id_bitacora = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, estado);
                if (calificacion == null) ps.setNull(2, Types.NUMERIC); else ps.setDouble(2, calificacion);
                ps.setLong(3, idBitacora);
                return ps.executeUpdate() > 0;
            }
        });
    }

    private Bitacora map(ResultSet rs) throws SQLException {
        Bitacora b = new Bitacora();
        b.setIdBitacora(rs.getLong("id_bitacora"));
        b.setIdEstudiante(rs.getLong("id_estudiante"));
        b.setIdPractica(rs.getLong("id_practica"));
        b.setEstado(rs.getString("estado"));
        b.setModalidad(rs.getString("modalidad"));
        Date fecha = rs.getDate("fecha_envio");
        if (fecha != null) b.setFechaEnvio(fecha.toLocalDate());
        double c = rs.getDouble("calificacion");
        b.setCalificacion(rs.wasNull() ? null : c);
        return b;
    }
}
