package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Pregunta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAO {

    public boolean agregarPregunta(Pregunta pr) {
        String sql = "INSERT INTO Pregunta (id_practica, enunciado, tipo_pregunta, obligatoria, orden) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pr.getIdPractica());
            ps.setString(2, pr.getEnunciado());
            ps.setString(3, pr.getTipoPregunta() != null ? pr.getTipoPregunta() : "Abierta");
            ps.setInt(4, pr.getObligatoria());
            ps.setInt(5, pr.getOrden());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Pregunta> listarPorPractica(int idPractica) {
        List<Pregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pregunta WHERE id_practica = ? ORDER BY orden";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPractica);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Pregunta> listarTodas() {
        List<Pregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pregunta ORDER BY id_practica, orden";
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean eliminarPregunta(int id) {
        String sql = "DELETE FROM Pregunta WHERE id_pregunta = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Pregunta mapRow(ResultSet rs) throws SQLException {
        Pregunta p = new Pregunta();
        p.setIdPregunta(rs.getInt("id_pregunta"));
        p.setIdPractica(rs.getInt("id_practica"));
        p.setEnunciado(rs.getString("enunciado"));
        p.setTipoPregunta(rs.getString("tipo_pregunta"));
        p.setObligatoria(rs.getInt("obligatoria"));
        p.setOrden(rs.getInt("orden"));
        return p;
    }
}
