package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Evidencia;
import java.sql.*;

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
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
            return false; 
        }
    }
}