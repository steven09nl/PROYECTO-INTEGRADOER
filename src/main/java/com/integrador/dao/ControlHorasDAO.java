package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.ControlHoras;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControlHorasDAO {

    public boolean registrarHoras(ControlHoras control) {
        String sql = "INSERT INTO Control_Horas (id_bitacora, fecha, hora_entrada, hora_salida, horas_cumplidas) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, control.getIdBitacora());
            ps.setDate(2, control.getFecha());
            ps.setTimestamp(3, control.getHoraEntrada());
            ps.setTimestamp(4, control.getHoraSalida());
            ps.setFloat(5, control.getHorasCumplidas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<ControlHoras> listarPorBitacora(int idBitacora) {
        List<ControlHoras> lista = new ArrayList<>();
        String sql = "SELECT * FROM Control_Horas WHERE id_bitacora = ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBitacora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ControlHoras ch = new ControlHoras();
                    ch.setIdRegistro(rs.getInt("id_registro"));
                    ch.setIdBitacora(rs.getInt("id_bitacora"));
                    ch.setFecha(rs.getDate("fecha"));
                    ch.setHoraEntrada(rs.getTimestamp("hora_entrada"));
                    ch.setHoraSalida(rs.getTimestamp("hora_salida"));
                    ch.setHorasCumplidas(rs.getFloat("horas_cumplidas"));
                    lista.add(ch);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /** RF-06: Suma total de horas cumplidas para una bitácora. */
    public float getTotalHorasPorBitacora(int idBitacora) {
        String sql = "SELECT NVL(SUM(horas_cumplidas),0) FROM Control_Horas WHERE id_bitacora=?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBitacora);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getFloat(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0f;
    }
}
