package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.Practica;

@Repository
public class PracticaRepository extends BaseRepository {
    public PracticaRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public List<Practica> findAll() {
        return withConnection(connection -> {
            List<Practica> list = new ArrayList<>();
            String sql = "SELECT id_practica, nombre, tipo_practica, horas_reglamentarias, estado, fecha_inicio, fecha_fin, semestre FROM Practica ORDER BY id_practica";
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        });
    }

    public List<Practica> findActive() {
        return withConnection(connection -> {
            List<Practica> list = new ArrayList<>();
            String sql = "SELECT id_practica, nombre, tipo_practica, horas_reglamentarias, estado, fecha_inicio, fecha_fin, semestre FROM Practica WHERE LOWER(estado) = 'activo' ORDER BY id_practica";
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        });
    }

    public Practica findById(Long id) {
        return withConnection(connection -> {
            String sql = "SELECT id_practica, nombre, tipo_practica, horas_reglamentarias, estado, fecha_inicio, fecha_fin, semestre FROM Practica WHERE id_practica = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? map(rs) : null;
                }
            }
        });
    }

    public Long save(Practica p) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Practica (nombre, tipo_practica, horas_reglamentarias, estado, fecha_inicio, fecha_fin, semestre) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getTipoPractica());
                if (p.getHorasReglamentarias() == null) ps.setNull(3, Types.NUMERIC); else ps.setInt(3, p.getHorasReglamentarias());
                ps.setString(4, p.getEstado());
                if (p.getFechaInicio() == null) ps.setNull(5, Types.DATE); else ps.setDate(5, Date.valueOf(p.getFechaInicio()));
                if (p.getFechaFin() == null) ps.setNull(6, Types.DATE); else ps.setDate(6, Date.valueOf(p.getFechaFin()));
                ps.setString(7, p.getSemestre());
                ps.executeUpdate();
                return currval(connection, "seq_practica");
            }
        });
    }

    public boolean update(Practica p) {
        return withConnection(connection -> {
            String sql = "UPDATE Practica SET nombre=?, tipo_practica=?, horas_reglamentarias=?, estado=?, fecha_inicio=?, fecha_fin=?, semestre=? WHERE id_practica=?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getTipoPractica());
                if (p.getHorasReglamentarias() == null) ps.setNull(3, Types.NUMERIC); else ps.setInt(3, p.getHorasReglamentarias());
                ps.setString(4, p.getEstado());
                if (p.getFechaInicio() == null) ps.setNull(5, Types.DATE); else ps.setDate(5, Date.valueOf(p.getFechaInicio()));
                if (p.getFechaFin() == null) ps.setNull(6, Types.DATE); else ps.setDate(6, Date.valueOf(p.getFechaFin()));
                ps.setString(7, p.getSemestre());
                ps.setLong(8, p.getIdPractica());
                return ps.executeUpdate() > 0;
            }
        });
    }

    private Practica map(ResultSet rs) throws SQLException {
        Practica p = new Practica();
        p.setIdPractica(rs.getLong("id_practica"));
        p.setNombre(rs.getString("nombre"));
        p.setTipoPractica(rs.getString("tipo_practica"));
        int horas = rs.getInt("horas_reglamentarias");
        p.setHorasReglamentarias(rs.wasNull() ? null : horas);
        p.setEstado(rs.getString("estado"));
        Date fi = rs.getDate("fecha_inicio");
        if (fi != null) p.setFechaInicio(fi.toLocalDate());
        Date ff = rs.getDate("fecha_fin");
        if (ff != null) p.setFechaFin(ff.toLocalDate());
        p.setSemestre(rs.getString("semestre"));
        return p;
    }
}
