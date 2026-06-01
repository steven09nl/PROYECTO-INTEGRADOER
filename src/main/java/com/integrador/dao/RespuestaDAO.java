package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Respuesta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RespuestaDAO {

    public boolean guardarRespuesta(Respuesta r) {
        String sql = "INSERT INTO Respuesta (id_pregunta,id_bitacora,texto_respuesta,fecha_respuesta) VALUES (?,?,?,?)";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt   (1, r.getIdPregunta());
            ps.setInt   (2, r.getIdBitacora());
            ps.setString(3, r.getTextoRespuesta());
            ps.setDate  (4, r.getFechaRespuesta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Devuelve filas: {id_respuesta, enunciado_pregunta, texto_respuesta, retroalimentacion} */
    public List<Object[]> listarConEnunciado(int idBitacora) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT r.id_respuesta, p.enunciado, r.texto_respuesta, r.retroalimentacion " +
                     "FROM Respuesta r JOIN Pregunta p ON r.id_pregunta=p.id_pregunta " +
                     "WHERE r.id_bitacora=? ORDER BY r.id_respuesta";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idBitacora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Object[]{
                    rs.getInt("id_respuesta"), rs.getString("enunciado"),
                    rs.getString("texto_respuesta"), rs.getString("retroalimentacion")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
