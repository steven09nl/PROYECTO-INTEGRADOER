package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.InstitucionReceptora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla INSTITUCION_RECEPTORA.
 * Gestiona las instituciones que reciben estudiantes en práctica.
 */
public class InstitucionReceptoraDAO {

    public boolean crear(InstitucionReceptora i) {
        String sql = "INSERT INTO Institucion_Receptora " +
                     "(nombre,nit,sector,direccion,ciudad,telefono,email_contacto," +
                     "nombre_coordinador,cargo_coordinador,estado,fecha_reg) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,'ACTIVO',SYSDATE)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, i.getNombre());
            setNullable(ps, 2, i.getNit());
            setNullable(ps, 3, i.getSector());
            setNullable(ps, 4, i.getDireccion());
            setNullable(ps, 5, i.getCiudad());
            setNullable(ps, 6, i.getTelefono());
            setNullable(ps, 7, i.getEmailContacto());
            setNullable(ps, 8, i.getNombreCoordinador());
            setNullable(ps, 9, i.getCargoCoordinador());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizar(InstitucionReceptora i) {
        String sql = "UPDATE Institucion_Receptora " +
                     "SET nombre=?,nit=?,sector=?,direccion=?,ciudad=?,telefono=?," +
                     "email_contacto=?,nombre_coordinador=?,cargo_coordinador=?,estado=? " +
                     "WHERE id_institucion=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, i.getNombre());
            setNullable(ps, 2, i.getNit());
            setNullable(ps, 3, i.getSector());
            setNullable(ps, 4, i.getDireccion());
            setNullable(ps, 5, i.getCiudad());
            setNullable(ps, 6, i.getTelefono());
            setNullable(ps, 7, i.getEmailContacto());
            setNullable(ps, 8, i.getNombreCoordinador());
            setNullable(ps, 9, i.getCargoCoordinador());
            ps.setString(10, i.getEstado() != null ? i.getEstado() : "ACTIVO");
            ps.setInt   (11, i.getIdInstitucion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminar(int id) {
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM Institucion_Receptora WHERE id_institucion=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<InstitucionReceptora> listarTodas() {
        List<InstitucionReceptora> lista = new ArrayList<>();
        String sql = "SELECT * FROM Institucion_Receptora ORDER BY nombre";
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<InstitucionReceptora> listarActivas() {
        List<InstitucionReceptora> lista = new ArrayList<>();
        String sql = "SELECT * FROM Institucion_Receptora WHERE estado='ACTIVO' ORDER BY nombre";
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private InstitucionReceptora mapRow(ResultSet rs) throws SQLException {
        InstitucionReceptora i = new InstitucionReceptora();
        i.setIdInstitucion    (rs.getInt   ("id_institucion"));
        i.setNombre           (rs.getString("nombre"));
        i.setNit              (rs.getString("nit"));
        i.setSector           (rs.getString("sector"));
        i.setDireccion        (rs.getString("direccion"));
        i.setCiudad           (rs.getString("ciudad"));
        i.setTelefono         (rs.getString("telefono"));
        i.setEmailContacto    (rs.getString("email_contacto"));
        i.setNombreCoordinador(rs.getString("nombre_coordinador"));
        i.setCargoCoordinador (rs.getString("cargo_coordinador"));
        i.setEstado           (rs.getString("estado"));
        i.setFechaReg         (rs.getDate  ("fecha_reg"));
        return i;
    }

    private void setNullable(PreparedStatement ps, int idx, String val) throws SQLException {
        if (val != null && !val.isBlank()) ps.setString(idx, val.trim());
        else ps.setNull(idx, Types.VARCHAR);
    }
}
