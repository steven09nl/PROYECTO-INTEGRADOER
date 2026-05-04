package com.proyecto.integrador.repository;

import com.proyecto.integrador.config.ConexionBD;
import com.proyecto.integrador.entity.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UsuarioRepository {

    public Usuario findByEmailAndPassword(String email, String password) {

        try {
            Connection conn = ConexionBD.getConexion();

            String sql = "SELECT * FROM USUARIO WHERE EMAIL = ? AND PASSWORD = ? AND ESTADO = 'ACTIVO'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getLong("ID_USUARIO"));
                u.setNombre(rs.getString("NOMBRE"));
                u.setEmail(rs.getString("EMAIL"));
                u.setPassword(rs.getString("PASSWORD"));
                u.setRol(rs.getString("ROL"));
                u.setEstado(rs.getString("ESTADO"));
                return u;
            }

        } catch (Exception e) {
            System.out.println("Error login Oracle: " + e.getMessage());
        }

        return null;
    }
}