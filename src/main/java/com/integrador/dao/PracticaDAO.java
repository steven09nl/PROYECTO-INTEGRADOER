package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Practica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PracticaDAO {

    public boolean crearPractica(Practica p) {
        String sql = "INSERT INTO Practica (nombre, tipo_practica, horas_reglamentarias, estado, fecha_inicio, fecha_fin, semestre) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipoPractica());
            ps.setInt(3, p.getHorasReglamentarias());
            ps.setString(4, p.getEstado() != null ? p.getEstado() : "Activa");
            ps.setDate(5, p.getFechaInicio());
            ps.setDate(6, p.getFechaFin());
            ps.setString(7, p.getSemestre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Practica> listarTodas() {
        List<Practica> lista = new ArrayList<>();
        String sql = "SELECT * FROM Practica ORDER BY id_practica DESC";
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Practica p = new Practica();
                p.setIdPractica(rs.getInt("id_practica"));
                p.setNombre(rs.getString("nombre"));
                p.setTipoPractica(rs.getString("tipo_practica"));
                p.setHorasReglamentarias(rs.getInt("horas_reglamentarias"));
                p.setEstado(rs.getString("estado"));
                p.setFechaInicio(rs.getDate("fecha_inicio"));
                p.setFechaFin(rs.getDate("fecha_fin"));
                p.setSemestre(rs.getString("semestre"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
