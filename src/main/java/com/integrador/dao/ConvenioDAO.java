package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Convenio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla CONVENIO.
 * RF-05: Los convenios tienen fecha de vencimiento; el estado se actualiza
 * automáticamente cuando la fecha actual supera la de vencimiento.
 */
public class ConvenioDAO {

    public boolean crear(Convenio cv) {
        String sql = "INSERT INTO Convenio " +
                     "(id_institucion,tipo_convenio,fecha_inicio,fecha_vencimiento,estado,descripcion,fecha_reg) " +
                     "VALUES (?,?,?,?,'VIGENTE',?,SYSDATE)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt (1, cv.getIdInstitucion());
            ps.setString(2, cv.getTipoConvenio());
            ps.setDate(3, cv.getFechaInicio());
            ps.setDate(4, cv.getFechaVencimiento());
            if (cv.getDescripcion() != null && !cv.getDescripcion().isBlank())
                ps.setString(5, cv.getDescripcion());
            else ps.setNull(5, Types.VARCHAR);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizar(Convenio cv) {
        String sql = "UPDATE Convenio " +
                     "SET id_institucion=?,tipo_convenio=?,fecha_inicio=?," +
                     "fecha_vencimiento=?,estado=?,descripcion=? " +
                     "WHERE id_convenio=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt   (1, cv.getIdInstitucion());
            ps.setString(2, cv.getTipoConvenio());
            ps.setDate  (3, cv.getFechaInicio());
            ps.setDate  (4, cv.getFechaVencimiento());
            ps.setString(5, cv.getEstado());
            if (cv.getDescripcion() != null && !cv.getDescripcion().isBlank())
                ps.setString(6, cv.getDescripcion());
            else ps.setNull(6, Types.VARCHAR);
            ps.setInt   (7, cv.getIdConvenio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminar(int id) {
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Convenio WHERE id_convenio=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Actualiza automáticamente a VENCIDO los convenios cuya fecha de vencimiento
     * ya pasó pero todavía figuran como VIGENTE. (RF-05)
     */
    public int actualizarVencidos() {
        String sql = "UPDATE Convenio SET estado='VENCIDO' " +
                     "WHERE fecha_vencimiento < SYSDATE AND estado='VIGENTE'";
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement()) {
            return st.executeUpdate(sql);
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    public List<Convenio> listarTodos() {
        actualizarVencidos(); // refrescar estados antes de listar
        List<Convenio> lista = new ArrayList<>();
        String sql = "SELECT cv.*, ir.nombre AS nombre_inst " +
                     "FROM Convenio cv " +
                     "LEFT JOIN Institucion_Receptora ir ON cv.id_institucion = ir.id_institucion " +
                     "ORDER BY cv.fecha_vencimiento DESC";
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private Convenio mapRow(ResultSet rs) throws SQLException {
        Convenio cv = new Convenio();
        cv.setIdConvenio      (rs.getInt   ("id_convenio"));
        cv.setIdInstitucion   (rs.getInt   ("id_institucion"));
        cv.setTipoConvenio    (rs.getString("tipo_convenio"));
        cv.setFechaInicio     (rs.getDate  ("fecha_inicio"));
        cv.setFechaVencimiento(rs.getDate  ("fecha_vencimiento"));
        cv.setEstado          (rs.getString("estado"));
        cv.setDescripcion     (rs.getString("descripcion"));
        cv.setFechaReg        (rs.getDate  ("fecha_reg"));
        try { cv.setNombreInstitucion(rs.getString("nombre_inst")); } catch (SQLException ignored) {}
        return cv;
    }
}
