package com.integrador.dao;

import com.integrador.config.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para GRUPO y GRUPO_ESTUDIANTE.
 * Ambas tablas tienen ahora trigger TRG_GRUPO / TRG_GRUPO_ESTUDIANTE.
 */
public class GrupoDAO {

    public boolean crearGrupo(String nombre, String semestre, int idDocente,
                               Date fechaInicio, String institucion) {
        String sql = "INSERT INTO Grupo (nombre,semestre,id_docente,fecha_inicio," +
                     "institucion,activo,fecha_reg) VALUES (?,?,?,?,?,'ACTIVO',SYSDATE)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, semestre);
            ps.setInt   (3, idDocente);
            ps.setDate  (4, fechaInicio);
            if (institucion != null && !institucion.isBlank())
                ps.setString(5, institucion);
            else
                ps.setNull(5, Types.VARCHAR);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizarGrupo(int idGrupo, String nombre, String semestre,
                                    int idDocente, String institucion) {
        String sql = "UPDATE Grupo SET nombre=?,semestre=?,id_docente=?,institucion=? WHERE id_grupo=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, semestre);
            ps.setInt   (3, idDocente);
            if (institucion != null && !institucion.isBlank()) ps.setString(4, institucion);
            else ps.setNull(4, Types.VARCHAR);
            ps.setInt(5, idGrupo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminarGrupo(int idGrupo) {
        Connection c = null;
        try {
            c = ConexionDB.getConnection(); c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM Grupo_Estudiante WHERE id_grupo=?")) {
                ps.setInt(1, idGrupo); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM Grupo WHERE id_grupo=?")) {
                ps.setInt(1, idGrupo); ps.executeUpdate();
            }
            c.commit(); return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (c != null) c.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (c != null) c.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean estudianteEnGrupoActivo(int idEstudiante) {
        String sql = "SELECT COUNT(*) FROM Grupo_Estudiante WHERE id_estudiante=? AND activo='ACTIVO'";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean agregarEstudiante(int idGrupo, int idEstudiante) {
        if (estudianteEnGrupoActivo(idEstudiante)) return false;
        String sql = "INSERT INTO Grupo_Estudiante (id_grupo,id_estudiante,fecha_ingreso,activo) " +
                     "VALUES (?,?,SYSDATE,'ACTIVO')";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idGrupo); ps.setInt(2, idEstudiante);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean moverEstudiante(int idEstudiante, int nuevoGrupo) {
        Connection c = null;
        try {
            c = ConexionDB.getConnection(); c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE Grupo_Estudiante SET activo='INACTIVO',fecha_salida=SYSDATE " +
                    "WHERE id_estudiante=? AND activo='ACTIVO'")) {
                ps.setInt(1, idEstudiante); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Grupo_Estudiante (id_grupo,id_estudiante,fecha_ingreso,activo) " +
                    "VALUES (?,?,SYSDATE,'ACTIVO')")) {
                ps.setInt(1, nuevoGrupo); ps.setInt(2, idEstudiante); ps.executeUpdate();
            }
            c.commit(); return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (c != null) c.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (c != null) c.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Object[]> listarGrupos() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT g.id_grupo, g.nombre, g.semestre, g.id_docente, " +
                     "u.nombre AS nd, g.fecha_inicio, g.institucion, g.activo " +
                     "FROM Grupo g LEFT JOIN Usuario u ON g.id_docente = u.id_usuario " +
                     "ORDER BY g.id_grupo";
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(new Object[]{
                rs.getInt("id_grupo"), rs.getString("nombre"), rs.getString("semestre"),
                rs.getInt("id_docente"), rs.getString("nd"),
                rs.getDate("fecha_inicio"), rs.getString("institucion"), rs.getString("activo")});
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Object[]> listarEstudiantesDeGrupo(int idGrupo) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre, u.email " +
                     "FROM Usuario u JOIN Grupo_Estudiante ge ON u.id_usuario = ge.id_estudiante " +
                     "WHERE ge.id_grupo=? AND ge.activo='ACTIVO' ORDER BY u.nombre";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Object[]{
                    rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("email")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
