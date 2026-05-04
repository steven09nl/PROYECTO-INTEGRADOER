package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.Respuesta;

@Repository
public class RespuestaRepository extends BaseRepository {
    public RespuestaRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public Long save(Respuesta r) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Respuesta (id_pregunta, id_bitacora, texto_respuesta, fecha_respuesta, retroalimentacion) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, r.getIdPregunta());
                ps.setLong(2, r.getIdBitacora());
                ps.setString(3, r.getTextoRespuesta());
                if (r.getFechaRespuesta() == null) ps.setNull(4, Types.DATE); else ps.setDate(4, Date.valueOf(r.getFechaRespuesta()));
                ps.setString(5, r.getRetroalimentacion());
                ps.executeUpdate();
                return currval(connection, "seq_respuesta");
            }
        });
    }

    public List<Respuesta> findByBitacora(Long idBitacora) {
        return withConnection(connection -> {
            List<Respuesta> list = new ArrayList<>();
            String sql = "SELECT id_respuesta, id_pregunta, id_bitacora, texto_respuesta, fecha_respuesta, retroalimentacion FROM Respuesta WHERE id_bitacora = ? ORDER BY id_respuesta";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idBitacora);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    public boolean updateFeedback(Long idRespuesta, String retroalimentacion) {
        return withConnection(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE Respuesta SET retroalimentacion = ? WHERE id_respuesta = ?")) {
                ps.setString(1, retroalimentacion);
                ps.setLong(2, idRespuesta);
                return ps.executeUpdate() > 0;
            }
        });
    }

    private Respuesta map(ResultSet rs) throws SQLException {
        Respuesta r = new Respuesta();
        r.setIdRespuesta(rs.getLong("id_respuesta"));
        r.setIdPregunta(rs.getLong("id_pregunta"));
        r.setIdBitacora(rs.getLong("id_bitacora"));
        r.setTextoRespuesta(rs.getString("texto_respuesta"));
        Date fecha = rs.getDate("fecha_respuesta");
        if (fecha != null) r.setFechaRespuesta(fecha.toLocalDate());
        r.setRetroalimentacion(rs.getString("retroalimentacion"));
        return r;
    }
}
