package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Pregunta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAO {

    public boolean agregarPregunta(Pregunta pr) {
        String sql = "INSERT INTO Pregunta (pregunta, id_practica) VALUES (?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pr.getPregunta());
            ps.setInt(2, pr.getIdPractica());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Pregunta> listarPorPractica(int idPractica) {
        List<Pregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pregunta WHERE id_practica = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPractica);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pregunta p = new Pregunta();
                    p.setIdPregunta(rs.getInt("id_pregunta"));
                    p.setPregunta(rs.getString("pregunta"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}