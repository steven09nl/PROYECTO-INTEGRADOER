package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.TipoPractica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla catálogo TIPO_PRACTICA.
 * La tabla SÍ tiene trigger (TRG_TIPO_PRACTICA), por lo que el
 * INSERT no necesita incluir el ID manualmente.
 */
public class TipoPracticaDAO {

    // ── Crear ────────────────────────────────────────────────────────────
    public boolean crear(TipoPractica t) {
        String sql = "INSERT INTO Tipo_Practica (nombre, descripcion, activo, fecha_reg) " +
                     "VALUES (?, ?, 'ACTIVO', SYSDATE)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getNombre().trim());
            if (t.getDescripcion() != null && !t.getDescripcion().isBlank())
                ps.setString(2, t.getDescripcion().trim());
            else
                ps.setNull(2, Types.VARCHAR);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Violación UNIQUE = nombre duplicado
            if (e.getErrorCode() == 1) return false; // ORA-00001
            e.printStackTrace();
            return false;
        }
    }

    // ── Actualizar ───────────────────────────────────────────────────────
    public boolean actualizar(TipoPractica t) {
        String sql = "UPDATE Tipo_Practica " +
                     "SET nombre=?, descripcion=?, activo=? " +
                     "WHERE id_tipo_practica=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getNombre().trim());
            if (t.getDescripcion() != null && !t.getDescripcion().isBlank())
                ps.setString(2, t.getDescripcion().trim());
            else
                ps.setNull(2, Types.VARCHAR);
            ps.setString(3, t.getActivo() != null ? t.getActivo() : "ACTIVO");
            ps.setInt   (4, t.getIdTipoPractica());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Eliminar (lógico — marca como INACTIVO) ──────────────────────────
    public boolean desactivar(int id) {
        String sql = "UPDATE Tipo_Practica SET activo='INACTIVO' WHERE id_tipo_practica=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Eliminar (físico) ─────────────────────────────────────────────────
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Tipo_Practica WHERE id_tipo_practica=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // ORA-02292: FK viola integridad — hay prácticas usando este tipo
            if (e.getErrorCode() == 2292) return false;
            e.printStackTrace();
            return false;
        }
    }

    // ── Listar activos (para ComboBox del formulario de Práctica) ─────────
    public List<TipoPractica> listarActivos() {
        return listar("WHERE activo='ACTIVO' ORDER BY nombre");
    }

    // ── Listar todos (para la tabla de gestión) ───────────────────────────
    public List<TipoPractica> listarTodos() {
        return listar("ORDER BY nombre");
    }

    // ── Buscar por nombre exacto ──────────────────────────────────────────
    public TipoPractica buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM Tipo_Practica WHERE UPPER(nombre)=UPPER(?) AND ROWNUM=1";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────
    public TipoPractica buscarPorId(int id) {
        String sql = "SELECT * FROM Tipo_Practica WHERE id_tipo_practica=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ── privados ──────────────────────────────────────────────────────────
    private List<TipoPractica> listar(String whereClause) {
        List<TipoPractica> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tipo_Practica " + whereClause;
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private TipoPractica mapRow(ResultSet rs) throws SQLException {
        TipoPractica t = new TipoPractica();
        t.setIdTipoPractica(rs.getInt   ("id_tipo_practica"));
        t.setNombre        (rs.getString("nombre"));
        t.setDescripcion   (rs.getString("descripcion"));
        t.setActivo        (rs.getString("activo"));
        t.setFechaReg      (rs.getDate  ("fecha_reg"));
        return t;
    }
}
