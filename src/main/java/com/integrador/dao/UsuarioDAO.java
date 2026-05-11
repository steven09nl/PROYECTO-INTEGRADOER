package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // ID del superusuario protegido que nunca puede eliminarse
    public static final int SUPERUSER_ID = 1;

    public Usuario loginEmail(String email, String password) {
        String sql = "SELECT * FROM Usuario WHERE TRIM(email) = ? AND TRIM(password) = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario ORDER BY id_usuario";
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean crearUsuario(Usuario u) {
        String sql = "INSERT INTO Usuario (nombre, email, password, rol, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRol());
            ps.setString(5, u.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE Usuario SET nombre=?, email=?, rol=?, estado=? WHERE id_usuario=?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getEstado());
            ps.setInt(5, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Eliminar usuario — rechaza si es el superusuario (ID=1) */
    public boolean eliminarUsuario(int id) {
        if (id == SUPERUSER_ID) return false;
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean esSuperUsuario(int id) {
        return id == SUPERUSER_ID;
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("rol"),
            rs.getString("estado")
        );
    }
}
