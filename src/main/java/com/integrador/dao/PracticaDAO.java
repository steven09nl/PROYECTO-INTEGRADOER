package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Practica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PracticaDAO {

    /**
     * Busca el id_tipo_practica por nombre (case-insensitive).
     * Si no existe, lo inserta en el catálogo y devuelve el nuevo ID.
     * Si todo falla devuelve 0 (se guardará NULL en id_tipo_practica).
     */
    private int obtenerOCrearIdTipo(Connection c, String nombreTipo) {
        if (nombreTipo == null || nombreTipo.isBlank()) return 0;
        String nombre = nombreTipo.trim();

        // 1. Buscar existente
        try {
            String sqlBuscar = "SELECT id_tipo_practica FROM Tipo_Practica " +
                               "WHERE UPPER(nombre) = UPPER(?) AND ROWNUM = 1";
            try (PreparedStatement ps = c.prepareStatement(sqlBuscar)) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. No existe → crearlo automáticamente
        try {
            String sqlInsert = "INSERT INTO Tipo_Practica (nombre, descripcion, activo, fecha_reg) " +
                               "VALUES (?, 'Tipo creado automáticamente', 'ACTIVO', SYSDATE)";
            try (PreparedStatement ps = c.prepareStatement(sqlInsert)) {
                ps.setString(1, nombre);
                ps.executeUpdate();
            }
            // Recuperar el ID recién creado
            String sqlId = "SELECT id_tipo_practica FROM Tipo_Practica " +
                           "WHERE UPPER(nombre) = UPPER(?) AND ROWNUM = 1";
            try (PreparedStatement ps = c.prepareStatement(sqlId)) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            // ORA-00001 = UNIQUE violation (ya fue creado por otro hilo) → reintentar búsqueda
            if (e.getErrorCode() == 1) {
                try {
                    String sqlRetry = "SELECT id_tipo_practica FROM Tipo_Practica " +
                                      "WHERE UPPER(nombre) = UPPER(?) AND ROWNUM = 1";
                    try (PreparedStatement ps = c.prepareStatement(sqlRetry)) {
                        ps.setString(1, nombre);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) return rs.getInt(1);
                        }
                    }
                } catch (SQLException ex) { ex.printStackTrace(); }
            } else {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public boolean crearPractica(Practica p) {
        String sql = "INSERT INTO Practica " +
                     "(nombre, tipo_practica, horas_reglamentarias, estado, " +
                     " fecha_inicio, fecha_fin, semestre, modalidad, id_grupo, id_tipo_practica) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipoPractica());
            ps.setInt   (3, p.getHorasReglamentarias());
            ps.setString(4, p.getEstado() != null ? p.getEstado() : "Activa");
            ps.setDate  (5, p.getFechaInicio());
            ps.setDate  (6, p.getFechaFin());
            ps.setString(7, p.getSemestre());
            ps.setString(8, p.getModalidad());

            if (p.getIdGrupo() > 0) ps.setInt(9, p.getIdGrupo());
            else                    ps.setNull(9, Types.INTEGER);

            int idTipo = obtenerOCrearIdTipo(c, p.getTipoPractica());
            if (idTipo > 0) ps.setInt(10, idTipo);
            else            ps.setNull(10, Types.INTEGER);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPractica(Practica p) {
        String sql = "UPDATE Practica " +
                     "SET nombre=?, tipo_practica=?, horas_reglamentarias=?, estado=?, " +
                     "    fecha_inicio=?, fecha_fin=?, semestre=?, modalidad=?, " +
                     "    id_grupo=?, id_tipo_practica=? " +
                     "WHERE id_practica=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipoPractica());
            ps.setInt   (3, p.getHorasReglamentarias());
            ps.setString(4, p.getEstado());
            ps.setDate  (5, p.getFechaInicio());
            ps.setDate  (6, p.getFechaFin());
            ps.setString(7, p.getSemestre());
            ps.setString(8, p.getModalidad());

            if (p.getIdGrupo() > 0) ps.setInt(9, p.getIdGrupo());
            else                    ps.setNull(9, Types.INTEGER);

            int idTipo = obtenerOCrearIdTipo(c, p.getTipoPractica());
            if (idTipo > 0) ps.setInt(10, idTipo);
            else            ps.setNull(10, Types.INTEGER);

            ps.setInt(11, p.getIdPractica());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarPractica(int id) {
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM Practica WHERE id_practica=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Practica> listarTodas() {
        List<Practica> lista = new ArrayList<>();
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT * FROM Practica ORDER BY id_practica DESC")) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<String> listarTipos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT tipo_practica FROM Practica " +
                     "WHERE tipo_practica IS NOT NULL ORDER BY tipo_practica";
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String t = rs.getString("tipo_practica");
                if (t != null && !t.isBlank()) lista.add(t.trim());
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private Practica mapRow(ResultSet rs) throws SQLException {
        Practica p = new Practica();
        p.setIdPractica         (rs.getInt   ("id_practica"));
        p.setNombre             (rs.getString("nombre"));
        p.setTipoPractica       (rs.getString("tipo_practica"));
        p.setHorasReglamentarias(rs.getInt   ("horas_reglamentarias"));
        p.setEstado             (rs.getString("estado"));
        p.setFechaInicio        (rs.getDate  ("fecha_inicio"));
        p.setFechaFin           (rs.getDate  ("fecha_fin"));
        p.setSemestre           (rs.getString("semestre"));
        p.setModalidad          (rs.getString("modalidad"));
        p.setIdGrupo            (rs.getInt   ("id_grupo"));
        return p;
    }
}
