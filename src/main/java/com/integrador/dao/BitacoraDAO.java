package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Bitacora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO {

    // Método para listar las bitácoras (ej. las que necesitan revisión)
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

    // Método para actualizar la nota y el estado de la bitácora
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
}