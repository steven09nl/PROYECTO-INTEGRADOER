package com.integrador.dao;

import com.integrador.config.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalificacionDAO {

    // Método para actualizar la calificación y el estado de una bitácora específica
    public boolean calificarBitacora(int idBitacora, double calificacion, String estado) {
        String sql = "UPDATE Bitacora SET calificacion = ?, estado = ? WHERE id_bitacora = ?";
        
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDouble(1, calificacion);
            ps.setString(2, estado);
            ps.setInt(3, idBitacora);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}