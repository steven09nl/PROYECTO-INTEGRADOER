package com.proyecto.integrador.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.proyecto.integrador.db.OracleConnectionFactory;
import com.proyecto.integrador.entity.ControlHoras;

@Repository
public class ControlHorasRepository extends BaseRepository {
    public ControlHorasRepository(OracleConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public Long save(ControlHoras c) {
        return withConnection(connection -> {
            String sql = "INSERT INTO Control_Horas (id_bitacora, fecha, hora_entrada, hora_salida, horas_cumplidas) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, c.getIdBitacora());
                if (c.getFecha() == null) ps.setNull(2, Types.DATE); else ps.setDate(2, Date.valueOf(c.getFecha()));
                if (c.getHoraEntrada() == null) ps.setNull(3, Types.TIME); else ps.setTime(3, Time.valueOf(c.getHoraEntrada()));
                if (c.getHoraSalida() == null) ps.setNull(4, Types.TIME); else ps.setTime(4, Time.valueOf(c.getHoraSalida()));
                if (c.getHorasCumplidas() == null) ps.setNull(5, Types.NUMERIC); else ps.setDouble(5, c.getHorasCumplidas());
                ps.executeUpdate();
                return currval(connection, "seq_control");
            }
        });
    }

    public List<ControlHoras> findByBitacora(Long idBitacora) {
        return withConnection(connection -> {
            List<ControlHoras> list = new ArrayList<>();
            String sql = "SELECT id_registro, id_bitacora, fecha, hora_entrada, hora_salida, horas_cumplidas FROM Control_Horas WHERE id_bitacora = ? ORDER BY id_registro DESC";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idBitacora);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            }
            return list;
        });
    }

    private ControlHoras map(ResultSet rs) throws SQLException {
        ControlHoras c = new ControlHoras();
        c.setIdRegistro(rs.getLong("id_registro"));
        c.setIdBitacora(rs.getLong("id_bitacora"));
        Date fecha = rs.getDate("fecha");
        if (fecha != null) c.setFecha(fecha.toLocalDate());
        Time entrada = rs.getTime("hora_entrada");
        if (entrada != null) c.setHoraEntrada(entrada.toLocalTime());
        Time salida = rs.getTime("hora_salida");
        if (salida != null) c.setHoraSalida(salida.toLocalTime());
        double h = rs.getDouble("horas_cumplidas");
        c.setHorasCumplidas(rs.wasNull() ? null : h);
        return c;
    }
}
