package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Evidencia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvidenciaDAO {

    public boolean guardarEvidencia(Evidencia e) {
        String sql = "INSERT INTO Evidencia (id_bitacora, url_archivo, fecha_carga, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, e.getIdBitacora());
            ps.setString(2, e.getUrlArchivo());
            ps.setDate(3, e.getFechaCarga());
            ps.setString(4, e.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public List<Evidencia> listarPorBitacora(int idBitacora) {
        List<Evidencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM Evidencia WHERE id_bitacora = ? ORDER BY fecha_carga DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBitacora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evidencia ev = new Evidencia();
                    ev.setIdEvidencias(rs.getInt("id_evidencias"));
                    ev.setIdBitacora(rs.getInt("id_bitacora"));
                    ev.setUrlArchivo(rs.getString("url_archivo"));
                    ev.setFechaCarga(rs.getDate("fecha_carga"));
                    ev.setDescripcion(rs.getString("descripcion"));
                    lista.add(ev);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
