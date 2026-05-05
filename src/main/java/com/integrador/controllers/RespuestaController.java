package com.integrador.controllers;

import com.integrador.dao.RespuestaDAO;
import com.integrador.models.Respuesta;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/api/respuestas")
public class RespuestaController extends HttpServlet {
    private RespuestaDAO dao = new RespuestaDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Respuesta r = new Respuesta();
            r.setIdPregunta(Integer.parseInt(req.getParameter("idPregunta")));
            r.setIdBitacora(Integer.parseInt(req.getParameter("idBitacora")));
            r.setTextoRespuesta(req.getParameter("textoRespuesta"));
            r.setFechaRespuesta(Date.valueOf(LocalDate.now())); // Fecha actual

            boolean ok = dao.guardarRespuesta(r);
            if (ok) {
                resp.getWriter().print("{\"status\":\"success\", \"mensaje\":\"Respuesta guardada.\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Error al guardar.\"}");
            }
        } catch (Exception e) {
            resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"" + e.getMessage() + "\"}");
        }
    }
}