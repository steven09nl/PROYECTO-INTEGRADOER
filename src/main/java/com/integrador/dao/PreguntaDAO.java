package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Pregunta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAO {

    public boolean agregarPregunta(Pregunta p) {
        String sql = "INSERT INTO Pregunta (id_practica,enunciado,tipo_pregunta,obligatoria,respuesta_correcta) VALUES (?,?,?,?,?)";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt   (1, p.getIdPractica());
            ps.setString(2, p.getEnunciado());
            ps.setString(3, p.getTipoPregunta() != null ? p.getTipoPregunta() : "Abierta");
            ps.setInt   (4, p.getObligatoria());
            ps.setString(5, p.getRespuestaCorrecta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizarPregunta(Pregunta p) {
        String sql = "UPDATE Pregunta SET enunciado=?,tipo_pregunta=?,obligatoria=?,respuesta_correcta=? WHERE id_pregunta=?";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getEnunciado());
            ps.setString(2, p.getTipoPregunta());
            ps.setInt   (3, p.getObligatoria());
            ps.setString(4, p.getRespuestaCorrecta());
            ps.setInt   (5, p.getIdPregunta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminarPregunta(int id) {
        Connection c = null;
        try {
            c = ConexionDB.getConnection(); c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM Opcion_Pregunta WHERE id_pregunta=?")) {
                ps.setInt(1, id); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM Pregunta WHERE id_pregunta=?")) {
                ps.setInt(1, id); ps.executeUpdate();
            }
            c.commit(); return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (c != null) c.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally { try { if (c != null) c.close(); } catch (SQLException e) { e.printStackTrace(); } }
    }

    public List<Pregunta> listarPorPractica(int idPractica) {
        List<Pregunta> lista = new ArrayList<>();
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM Pregunta WHERE id_practica=?")) {
            ps.setInt(1, idPractica);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(mapRow(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Pregunta> listarTodas() {
        List<Pregunta> lista = new ArrayList<>();
        try (Connection c = ConexionDB.getConnection(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Pregunta ORDER BY id_practica, id_pregunta")) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /** Devuelve el ID generado o -1 si falló. */
    public int agregarPreguntaReturnId(Pregunta p) {
        String sql = "INSERT INTO Pregunta (id_practica,enunciado,tipo_pregunta,obligatoria,respuesta_correcta) VALUES (?,?,?,?,?)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, new String[]{"ID_PREGUNTA"})) {
            ps.setInt   (1, p.getIdPractica());
            ps.setString(2, p.getEnunciado());
            ps.setString(3, p.getTipoPregunta() != null ? p.getTipoPregunta() : "Abierta");
            ps.setInt   (4, p.getObligatoria());
            ps.setString(5, p.getRespuestaCorrecta());
            if (ps.executeUpdate() > 0) {
                try (ResultSet gen = ps.getGeneratedKeys()) { if (gen.next()) return gen.getInt(1); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    private Pregunta mapRow(ResultSet rs) throws SQLException {
        Pregunta p = new Pregunta();
        p.setIdPregunta      (rs.getInt   ("id_pregunta"));
        p.setIdPractica      (rs.getInt   ("id_practica"));
        p.setEnunciado       (rs.getString("enunciado"));
        p.setTipoPregunta    (rs.getString("tipo_pregunta"));
        p.setObligatoria     (rs.getInt   ("obligatoria"));
        p.setRespuestaCorrecta(rs.getString("respuesta_correcta"));
        return p;
    }
}
