package com.integrador.dao;

import com.integrador.config.ConexionDB;
import com.integrador.models.Observacion;
import java.sql.*;

public class ObservacionDAO {

    public boolean agregarObservacion(Observacion obs) {
        // id_observacion es insertado por el trigger trg_observacion
        String sql = "INSERT INTO Observacion (id_bitacora, id_asesor, texto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, obs.getIdBitacora());
            ps.setInt(2, obs.getIdAsesor());
            ps.setString(3, obs.getTexto());
            ps.setDate(4, obs.getFecha());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}