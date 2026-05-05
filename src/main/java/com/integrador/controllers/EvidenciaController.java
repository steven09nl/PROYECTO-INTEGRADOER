package com.integrador.controllers;

import com.integrador.dao.EvidenciaDAO;
import com.integrador.models.Evidencia;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/api/evidencias")
public class EvidenciaController extends HttpServlet {
    private EvidenciaDAO dao = new EvidenciaDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Evidencia e = new Evidencia();
            e.setIdBitacora(Integer.parseInt(req.getParameter("idBitacora")));
            e.setUrlArchivo(req.getParameter("urlArchivo")); // Puede ser una URL de Drive, Dropbox, etc.
            e.setDescripcion(req.getParameter("descripcion"));
            e.setFechaCarga(Date.valueOf(LocalDate.now())); // Fecha actual

            boolean ok = dao.guardarEvidencia(e);
            if (ok) {
                resp.getWriter().print("{\"status\":\"success\", \"mensaje\":\"Evidencia subida correctamente.\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Error al subir la evidencia.\"}");
            }
        } catch (Exception ex) {
            resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"" + ex.getMessage() + "\"}");
        }
    }
}