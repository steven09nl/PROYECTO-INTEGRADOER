package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Informe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InformeDAO {

    public boolean registrarInforme(Informe info) {
        String sql = "INSERT INTO Informe (id_usuario_gen, tipo_informe, fecha_generacion, periodo, url_archivo) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, info.getIdUsuarioGen());
            ps.setString(2, info.getTipoInforme());
            ps.setDate(3, info.getFechaGeneracion());
            ps.setString(4, info.getPeriodo());
            ps.setString(5, info.getUrlArchivo());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Informe> listarInformes() {
        List<Informe> lista = new ArrayList<>();
        String sql = "SELECT * FROM Informe ORDER BY fecha_generacion DESC";
        
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Informe info = new Informe();
                info.setIdInforme(rs.getInt("id_informe"));
                info.setIdUsuarioGen(rs.getInt("id_usuario_gen"));
                info.setTipoInforme(rs.getString("tipo_informe"));
                info.setFechaGeneracion(rs.getDate("fecha_generacion"));
                info.setPeriodo(rs.getString("periodo"));
                info.setUrlArchivo(rs.getString("url_archivo"));
                lista.add(info);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}