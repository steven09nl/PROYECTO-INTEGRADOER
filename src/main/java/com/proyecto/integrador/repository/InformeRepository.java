package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.Informe;

@Repository
public class InformeRepository extends BaseRepository {
    public InformeRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public Long save(Informe i) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Informe (id_usuario_gen, tipo_informe, fecha_generacion, periodo, url_archivo) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, i.getIdUsuarioGen());
                ps.setString(2, i.getTipoInforme());
                if (i.getFechaGeneracion() == null) ps.setNull(3, Types.DATE); else ps.setDate(3, Date.valueOf(i.getFechaGeneracion()));
                ps.setString(4, i.getPeriodo());
                ps.setString(5, i.getUrlArchivo());
                ps.executeUpdate();
                return currval(connection, "seq_informe");
            }
        });
    }

    public List<Informe> findByUsuario(Long idUsuario) {
        return withConnection(connection -> {
            List<Informe> list = new ArrayList<>();
            String sql = "SELECT id_informe, id_usuario_gen, tipo_informe, fecha_generacion, periodo, url_archivo FROM Informe WHERE id_usuario_gen = ? ORDER BY id_informe DESC";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    public List<Informe> findAll() {
        return withConnection(connection -> {
            List<Informe> list = new ArrayList<>();
            String sql = "SELECT id_informe, id_usuario_gen, tipo_informe, fecha_generacion, periodo, url_archivo FROM Informe ORDER BY id_informe DESC";
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        });
    }

    private Informe map(ResultSet rs) throws SQLException {
        Informe i = new Informe();
        i.setIdInforme(rs.getLong("id_informe"));
        i.setIdUsuarioGen(rs.getLong("id_usuario_gen"));
        i.setTipoInforme(rs.getString("tipo_informe"));
        Date fecha = rs.getDate("fecha_generacion");
        if (fecha != null) i.setFechaGeneracion(fecha.toLocalDate());
        i.setPeriodo(rs.getString("periodo"));
        i.setUrlArchivo(rs.getString("url_archivo"));
        return i;
    }
}
