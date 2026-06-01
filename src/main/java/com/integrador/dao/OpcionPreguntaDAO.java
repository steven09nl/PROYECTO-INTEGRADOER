package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.OpcionPregunta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para OPCION_PREGUNTA.
 * Tiene trigger TRG_OPCION_PREGUNTA — el INSERT no necesita el ID.
 */
public class OpcionPreguntaDAO {

    public boolean agregarOpcion(int idPregunta, String texto, int esCorrecta) {
        String sql = "INSERT INTO Opcion_Pregunta (id_pregunta,texto_opcion,es_correcta) VALUES (?,?,?)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPregunta); ps.setString(2, texto); ps.setInt(3, esCorrecta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<OpcionPregunta> listarPorPregunta(int idPregunta) {
        List<OpcionPregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Opcion_Pregunta WHERE id_pregunta=? ORDER BY id_opcion";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPregunta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OpcionPregunta o = new OpcionPregunta();
                    o.setIdOpcion   (rs.getInt   ("id_opcion"));
                    o.setIdPregunta (rs.getInt   ("id_pregunta"));
                    o.setTextoOpcion(rs.getString("texto_opcion"));
                    o.setEsCorrecta (rs.getInt   ("es_correcta"));
                    lista.add(o);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean eliminarPorPregunta(int idPregunta) {
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM Opcion_Pregunta WHERE id_pregunta=?")) {
            ps.setInt(1, idPregunta); ps.executeUpdate(); return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
