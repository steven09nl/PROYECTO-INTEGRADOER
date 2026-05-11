package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Respuesta;
import java.sql.*;

public class RespuestaDAO {

    public boolean guardarRespuesta(Respuesta r) {
        String sql = "INSERT INTO Respuesta (id_pregunta, id_bitacora, texto_respuesta, fecha_respuesta) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getIdPregunta());
            ps.setInt(2, r.getIdBitacora());
            ps.setString(3, r.getTextoRespuesta());
            ps.setDate(4, r.getFechaRespuesta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}