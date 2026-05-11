package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Observacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObservacionDAO {

    public boolean agregarObservacion(Observacion obs) {
        String sql = "INSERT INTO Observacion (id_bitacora, id_asesor, texto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, obs.getIdBitacora());
            ps.setInt(2, obs.getIdAsesor());
            ps.setString(3, obs.getTexto());
            ps.setDate(4, obs.getFecha());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Observacion> listarPorAsesor(int idAsesor) {
        List<Observacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Observacion WHERE id_asesor = ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsesor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Observacion> listarPorBitacora(int idBitacora) {
        List<Observacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Observacion WHERE id_bitacora = ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBitacora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private Observacion mapRow(ResultSet rs) throws SQLException {
        Observacion o = new Observacion();
        o.setIdObservacion(rs.getInt("id_observacion"));
        o.setIdBitacora(rs.getInt("id_bitacora"));
        o.setIdAsesor(rs.getInt("id_asesor"));
        o.setTexto(rs.getString("texto"));
        o.setFecha(rs.getDate("fecha"));
        return o;
    }
}
