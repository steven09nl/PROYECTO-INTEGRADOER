package com.integrador.controllers;

import com.google.gson.Gson;
import com.integrador.dao.PreguntaDAO;
import com.integrador.models.Pregunta;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/preguntas")
public class PreguntaController extends HttpServlet {
    
    private PreguntaDAO dao = new PreguntaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        String idPracticaStr = req.getParameter("idPractica");
        
        if (idPracticaStr != null && !idPracticaStr.isEmpty()) {
            int idPractica = Integer.parseInt(idPracticaStr);
            List<Pregunta> lista = dao.listarPorPractica(idPractica);
            resp.getWriter().print(gson.toJson(lista));
        } else {
            resp.getWriter().print("[]"); // Retorna lista vacía si no hay ID
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Pregunta pr = new Pregunta();
            pr.setPregunta(req.getParameter("pregunta"));
            pr.setIdPractica(Integer.parseInt(req.getParameter("idPractica")));

            boolean ok = dao.agregarPregunta(pr);
            
            if (ok) {
                out.print("{\"status\":\"success\", \"mensaje\":\"Pregunta guardada correctamente.\"}");
            } else {
                out.print("{\"status\":\"error\", \"mensaje\":\"Error al guardar la pregunta.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\", \"mensaje\":\"" + e.getMessage() + "\"}");
        }
        
        out.flush();
    }
}