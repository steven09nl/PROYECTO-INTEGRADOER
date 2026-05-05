package com.integrador.controllers;
import com.integrador.dao.CalificacionDAO;

import com.google.gson.Gson;
import com.integrador.dao.BitacoraDAO;
import com.integrador.dao.ObservacionDAO;
import com.integrador.models.Bitacora;
import com.integrador.models.Observacion;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/calificaciones")
public class CalificacionController extends HttpServlet {
    
    private BitacoraDAO bitacoraDAO = new BitacoraDAO();
    private ObservacionDAO observacionDAO = new ObservacionDAO();
    private Gson gson = new Gson();

    // Obtener lista de bitácoras para evaluar
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        List<Bitacora> lista = bitacoraDAO.listarBitacoras();
        resp.getWriter().print(gson.toJson(lista));
    }

    // Registrar calificación y observación
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int idBitacora = Integer.parseInt(req.getParameter("idBitacora"));
            int idAsesor = Integer.parseInt(req.getParameter("idAsesor"));
            double calificacion = Double.parseDouble(req.getParameter("calificacion"));
            String estado = req.getParameter("estado"); // Ej. "Aprobada", "Reprobada", "Requiere correcciones"
            String textoObservacion = req.getParameter("observacion");

            // 1. Actualizar Bitácora
            boolean bitacoraOk = new CalificacionDAO().calificarBitacora(idBitacora, calificacion, estado);

            // 2. Guardar Observación (Retroalimentación)
            boolean observacionOk = false;
            if (textoObservacion != null && !textoObservacion.trim().isEmpty()) {
                Observacion obs = new Observacion();
                obs.setIdBitacora(idBitacora);
                obs.setIdAsesor(idAsesor);
                obs.setTexto(textoObservacion);
                obs.setFecha(Date.valueOf(LocalDate.now()));
                observacionOk = observacionDAO.agregarObservacion(obs);
            } else {
                observacionOk = true; // Si no hay observación, no lo consideramos un error.
            }

            if (bitacoraOk && observacionOk) {
                resp.getWriter().print("{\"status\":\"success\", \"mensaje\":\"Calificación y observación registradas correctamente.\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Ocurrió un problema al guardar los datos.\"}");
            }

        } catch (Exception e) {
            resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"" + e.getMessage() + "\"}");
        }
    }
}