package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.ControlHoras;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControlHorasDAO {

    public boolean registrarHoras(ControlHoras control) {
        // No enviamos id_registro porque el trigger trg_control lo maneja 
        String sql = "INSERT INTO Control_Horas (id_bitacora, fecha, hora_entrada, hora_salida, horas_cumplidas) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, control.getIdBitacora());
            ps.setDate(2, control.getFecha());
            ps.setTimestamp(3, control.getHoraEntrada());
            ps.setTimestamp(4, control.getHoraSalida());
            ps.setFloat(5, control.getHorasCumplidas());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}