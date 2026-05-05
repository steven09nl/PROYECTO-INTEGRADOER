package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Método reflejado en tu diagrama de secuencia de Autenticación
    public Usuario loginEmail(String email, String password) {
        Usuario usuario = null;
        String sql = "SELECT * FROM Usuario WHERE email = ? AND password = ?"; // 
        
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("rol"),
                        rs.getString("estado")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario; // Retorna nulo si las credenciales son inválidas
    }
}