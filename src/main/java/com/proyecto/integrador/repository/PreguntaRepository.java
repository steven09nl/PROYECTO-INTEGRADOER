package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.Pregunta;

@Repository
public class PreguntaRepository extends BaseRepository {
    public PreguntaRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public List<Pregunta> findByPractica(Long idPractica) {
        return withConnection(connection -> {
            List<Pregunta> list = new ArrayList<>();
            String sql = "SELECT id_pregunta, id_practica, enunciado, tipo_pregunta, obligatoria, orden FROM Pregunta WHERE id_practica = ? ORDER BY orden, id_pregunta";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idPractica);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    public Long save(Pregunta p) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Pregunta (id_practica, enunciado, tipo_pregunta, obligatoria, orden) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, p.getIdPractica());
                ps.setString(2, p.getEnunciado());
                ps.setString(3, p.getTipoPregunta());
                ps.setInt(4, Boolean.TRUE.equals(p.getObligatoria()) ? 1 : 0);
                if (p.getOrden() == null) ps.setNull(5, Types.NUMERIC); else ps.setInt(5, p.getOrden());
                ps.executeUpdate();
                return currval(connection, "seq_pregunta");
            }
        });
    }

    public boolean reorder(Long idPregunta, int nuevoOrden) {
        return withConnection(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE Pregunta SET orden = ? WHERE id_pregunta = ?")) {
                ps.setInt(1, nuevoOrden);
                ps.setLong(2, idPregunta);
                return ps.executeUpdate() > 0;
            }
        });
    }

    private Pregunta map(ResultSet rs) throws SQLException {
        Pregunta p = new Pregunta();
        p.setIdPregunta(rs.getLong("id_pregunta"));
        p.setIdPractica(rs.getLong("id_practica"));
        p.setEnunciado(rs.getString("enunciado"));
        p.setTipoPregunta(rs.getString("tipo_pregunta"));
        p.setObligatoria(rs.getInt("obligatoria") == 1);
        int orden = rs.getInt("orden");
        p.setOrden(rs.wasNull() ? null : orden);
        return p;
    }
}
