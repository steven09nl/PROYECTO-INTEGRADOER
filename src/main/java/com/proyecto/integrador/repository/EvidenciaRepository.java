package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.Evidencia;

@Repository
public class EvidenciaRepository extends BaseRepository {
    public EvidenciaRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public Long save(Evidencia e) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Evidencia (id_bitacora, url_archivo, fecha_carga, descripcion) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, e.getIdBitacora());
                ps.setString(2, e.getUrlArchivo());
                if (e.getFechaCarga() == null) ps.setNull(3, Types.DATE); else ps.setDate(3, Date.valueOf(e.getFechaCarga()));
                ps.setString(4, e.getDescripcion());
                ps.executeUpdate();
                return currval(connection, "seq_evidencia");
            }
        });
    }

    public List<Evidencia> findByBitacora(Long idBitacora) {
        return withConnection(connection -> {
            List<Evidencia> list = new ArrayList<>();
            String sql = "SELECT id_evidencias, id_bitacora, url_archivo, fecha_carga, descripcion FROM Evidencia WHERE id_bitacora = ? ORDER BY id_evidencias DESC";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idBitacora);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    private Evidencia map(ResultSet rs) throws SQLException {
        Evidencia e = new Evidencia();
        e.setIdEvidencia(rs.getLong("id_evidencias"));
        e.setIdBitacora(rs.getLong("id_bitacora"));
        e.setUrlArchivo(rs.getString("url_archivo"));
        Date fecha = rs.getDate("fecha_carga");
        if (fecha != null) e.setFechaCarga(fecha.toLocalDate());
        e.setDescripcion(rs.getString("descripcion"));
        return e;
    }
}
