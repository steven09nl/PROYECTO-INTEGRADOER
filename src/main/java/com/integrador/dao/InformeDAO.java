package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Informe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para INFORME.
 * La tabla tiene trigger TRG_INFORME, por lo que el INSERT
 * NO necesita incluir el ID manualmente.
 */
public class InformeDAO {

    public boolean registrarInforme(Informe i) {
        String sql = "INSERT INTO Informe " +
                     "(id_usuario_gen,tipo_informe,fecha_generacion,periodo,url_archivo) " +
                     "VALUES (?,?,?,?,?)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt   (1, i.getIdUsuarioGen());
            ps.setString(2, i.getTipoInforme());
            ps.setDate  (3, i.getFechaGeneracion());
            ps.setString(4, i.getPeriodo());
            ps.setString(5, i.getUrlArchivo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizarInforme(Informe i) {
        String sql = "UPDATE Informe SET tipo_informe=?,periodo=?,url_archivo=? WHERE id_informe=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, i.getTipoInforme());
            ps.setString(2, i.getPeriodo());
            ps.setString(3, i.getUrlArchivo());
            ps.setInt   (4, i.getIdInforme());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminarInforme(int id) {
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Informe WHERE id_informe=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Informe> listarInformes() {
        List<Informe> lista = new ArrayList<>();
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Informe ORDER BY fecha_generacion DESC")) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /** Consulta consolidada para exportar reporte por período. */
    public List<Object[]> consultaConsolidada(String periodo) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT u.nombre, u.email, p.nombre AS practica, " +
                     "b.estado, b.calificacion, b.modalidad " +
                     "FROM Bitacora b " +
                     "JOIN Usuario u ON b.id_estudiante = u.id_usuario " +
                     "JOIN Practica p ON b.id_practica  = p.id_practica " +
                     "WHERE p.semestre = ? ORDER BY u.nombre";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, periodo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Object[]{
                    rs.getString("nombre"),      rs.getString("email"),
                    rs.getString("practica"),    rs.getString("estado"),
                    rs.getDouble("calificacion"),rs.getString("modalidad")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private Informe mapRow(ResultSet rs) throws SQLException {
        Informe i = new Informe();
        i.setIdInforme      (rs.getInt   ("id_informe"));
        i.setIdUsuarioGen   (rs.getInt   ("id_usuario_gen"));
        i.setTipoInforme    (rs.getString("tipo_informe"));
        i.setFechaGeneracion(rs.getDate  ("fecha_generacion"));
        i.setPeriodo        (rs.getString("periodo"));
        i.setUrlArchivo     (rs.getString("url_archivo"));
        return i;
    }
}
