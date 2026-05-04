package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.Observacion;

@Repository
public class ObservacionRepository extends BaseRepository {
    public ObservacionRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public Long save(Observacion o) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Observacion (id_bitacora, id_asesor, texto, fecha) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, o.getIdBitacora());
                ps.setLong(2, o.getIdAsesor());
                ps.setString(3, o.getTexto());
                if (o.getFecha() == null) ps.setNull(4, Types.DATE); else ps.setDate(4, Date.valueOf(o.getFecha()));
                ps.executeUpdate();
                return currval(connection, "seq_observacion");
            }
        });
    }

    public List<Observacion> findByBitacora(Long idBitacora) {
        return withConnection(connection -> {
            List<Observacion> list = new ArrayList<>();
            String sql = "SELECT id_observacion, id_bitacora, id_asesor, texto, fecha FROM Observacion WHERE id_bitacora = ? ORDER BY id_observacion DESC";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idBitacora);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    private Observacion map(ResultSet rs) throws SQLException {
        Observacion o = new Observacion();
        o.setIdObservacion(rs.getLong("id_observacion"));
        o.setIdBitacora(rs.getLong("id_bitacora"));
        o.setIdAsesor(rs.getLong("id_asesor"));
        o.setTexto(rs.getString("texto"));
        Date fecha = rs.getDate("fecha");
        if (fecha != null) o.setFecha(fecha.toLocalDate());
        return o;
    }
}
