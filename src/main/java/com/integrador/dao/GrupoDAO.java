package com.integrador.dao;

import com.integrador.config.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO {

    /**
     * Elimina un grupo pero conserva los estudiantes (solo desvincula la relación).
     * Regla: se puede eliminar el grupo, pero el estudiante debe quedar.
     */
    public boolean eliminarGrupo(int idGrupo) {
        Connection con = null;
        try {
            con = ConexionDB.getConnection();
            con.setAutoCommit(false);

            // 1. Desvincular estudiantes del grupo (no eliminarlos)
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM Grupo_Estudiante WHERE id_grupo = ?")) {
                ps.setInt(1, idGrupo);
                ps.executeUpdate();
            }

            // 2. Eliminar el grupo
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM Grupo WHERE id_grupo = ?")) {
                ps.setInt(1, idGrupo);
                ps.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /** Listar todos los grupos */
    public List<Object[]> listarGrupos() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT g.id_grupo, g.nombre, g.semestre, g.id_docente, " +
                     "u.nombre as nombre_docente, g.fecha_inicio " +
                     "FROM Grupo g LEFT JOIN Usuario u ON g.id_docente = u.id_usuario " +
                     "ORDER BY g.id_grupo";
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_grupo"),
                    rs.getString("nombre"),
                    rs.getString("semestre"),
                    rs.getInt("id_docente"),
                    rs.getString("nombre_docente"),
                    rs.getDate("fecha_inicio")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /** Listar estudiantes de un grupo */
    public List<Object[]> listarEstudiantesDeGrupo(int idGrupo) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre, u.email " +
                     "FROM Usuario u " +
                     "JOIN Grupo_Estudiante ge ON u.id_usuario = ge.id_usuario " +
                     "WHERE ge.id_grupo = ? ORDER BY u.nombre";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email")
                    });
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /** Crear grupo */
    public boolean crearGrupo(String nombre, String semestre, int idDocente, Date fechaInicio) {
        String sql = "INSERT INTO Grupo (nombre, semestre, id_docente, fecha_inicio) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, semestre);
            ps.setInt(3, idDocente);
            ps.setDate(4, fechaInicio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Agregar estudiante a grupo */
    public boolean agregarEstudiante(int idGrupo, int idUsuario) {
        String sql = "INSERT INTO Grupo_Estudiante (id_grupo, id_usuario) VALUES (?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
