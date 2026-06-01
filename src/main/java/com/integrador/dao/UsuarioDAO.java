package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Usuario;
import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static final int SUPERUSER_ID = 1;

    public Usuario loginEmail(String email, String password) {
        String sql = "SELECT * FROM Usuario WHERE TRIM(email)=? AND TRIM(password)=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email); ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> list = new ArrayList<>();
        try (Connection c = ConexionDB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Usuario ORDER BY id_usuario")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean crearUsuario(Usuario u) {
        String sql = "INSERT INTO Usuario (nombre,email,password,rol,estado) VALUES (?,?,?,?,?)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRol());
            ps.setString(5, u.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Actualiza nombre, email, rol, estado y — si no está vacío — la contraseña. */
    public boolean actualizarUsuario(Usuario u, String nuevaPassword) {
        boolean cambiaPass = nuevaPassword != null && !nuevaPassword.isBlank();
        String sql = cambiaPass
            ? "UPDATE Usuario SET nombre=?,email=?,rol=?,estado=?,password=? WHERE id_usuario=?"
            : "UPDATE Usuario SET nombre=?,email=?,rol=?,estado=? WHERE id_usuario=?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getEstado());
            if (cambiaPass) { ps.setString(5, nuevaPassword); ps.setInt(6, u.getIdUsuario()); }
            else              ps.setInt(5, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminarUsuario(int id) {
        if (id == SUPERUSER_ID) return false;
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Usuario WHERE id_usuario=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean esSuperUsuario(int id) { return id == SUPERUSER_ID; }

    /**
     * Genera email único: 1ª letra nombre + 1er apellido + número consecutivo.
     * Ejemplo: "Nicolás León García" → "nleon6@udi.edu.co"
     */
    public String generarEmail(String nombre) {
        if (nombre == null || nombre.isBlank()) return "";
        String clean = Normalizer.normalize(nombre.toLowerCase().trim(), Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replaceAll("[^a-z0-9 ]", "").trim();
        String[] parts = clean.split("\\s+");
        String inicial  = parts.length > 0 ? String.valueOf(parts[0].charAt(0)) : "";
        String apellido = parts.length > 1 ? parts[1] : (parts.length > 0 ? parts[0] : "usuario");
        String base = inicial + apellido;
        // buscar número consecutivo
        int num = nextConsecutive(base);
        return base + num + "@udi.edu.co";
    }

    private int nextConsecutive(String base) {
        String sql = "SELECT email FROM Usuario WHERE email LIKE ?";
        int max = 0;
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, base + "%@udi.edu.co");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String em = rs.getString("email");
                    String mid = em.replace(base, "").replace("@udi.edu.co", "");
                    try { int n = Integer.parseInt(mid); if (n > max) max = n; } catch (Exception ignored) {}
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return max + 1;
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
            rs.getString("email"), rs.getString("password"),
            rs.getString("rol"),   rs.getString("estado"));
    }
}
