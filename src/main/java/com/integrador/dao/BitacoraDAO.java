package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Bitacora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO {

    public List<Bitacora> listarBitacoras() {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT * FROM Bitacora ORDER BY fecha_envio DESC";
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Bitacora b = new Bitacora();
                b.setIdBitacora(rs.getInt("id_bitacora"));
                b.setIdEstudiante(rs.getInt("id_estudiante"));
                b.setIdPractica(rs.getInt("id_practica"));
                b.setEstado(rs.getString("estado"));
                b.setModalidad(rs.getString("modalidad"));
                b.setFechaEnvio(rs.getDate("fecha_envio"));
                b.setCalificacion(rs.getDouble("calificacion"));
                lista.add(b);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean calificarBitacora(int idBitacora, double calificacion, String estado) {
        String sql = "UPDATE Bitacora SET calificacion = ?, estado = ? WHERE id_bitacora = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, calificacion);
            ps.setString(2, estado);
            ps.setInt(3, idBitacora);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una bitácora y en cascada sus preguntas asociadas.
     * Regla: si se elimina la bitácora, se elimina también la pregunta.
     */
    public boolean eliminarBitacora(int idBitacora) {
        Connection con = null;
        try {
            con = ConexionDB.getConnection();
            con.setAutoCommit(false);

            // 1. Eliminar respuestas ligadas a esta bitácora
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM Respuesta WHERE id_bitacora = ?")) {
                ps.setInt(1, idBitacora);
                ps.executeUpdate();
            }

            // 2. Eliminar observaciones de esta bitácora
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM Observacion WHERE id_bitacora = ?")) {
                ps.setInt(1, idBitacora);
                ps.executeUpdate();
            }

            // 3. Eliminar evidencias de esta bitácora
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM Evidencia WHERE id_bitacora = ?")) {
                ps.setInt(1, idBitacora);
                ps.executeUpdate();
            }

            // 4. Eliminar control de horas de esta bitácora
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM Control_Horas WHERE id_bitacora = ?")) {
                ps.setInt(1, idBitacora);
                ps.executeUpdate();
            }

            // 5. Eliminar las preguntas asociadas a la práctica de esta bitácora
            //    (obtenemos el id_practica de la bitácora primero)
            int idPractica = -1;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT id_practica FROM Bitacora WHERE id_bitacora = ?")) {
                ps.setInt(1, idBitacora);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) idPractica = rs.getInt("id_practica");
                }
            }
            if (idPractica > 0) {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM Pregunta WHERE id_practica = ?")) {
                    ps.setInt(1, idPractica);
                    ps.executeUpdate();
                }
            }

            // 6. Eliminar la bitácora
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM Bitacora WHERE id_bitacora = ?")) {
                ps.setInt(1, idBitacora);
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
}
