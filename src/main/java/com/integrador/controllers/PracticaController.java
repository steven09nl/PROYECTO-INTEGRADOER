package com.integrador.controllers;

import com.google.gson.Gson;
import com.integrador.dao.PracticaDAO;
import com.integrador.models.Practica;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/api/practicas")
public class PracticaController extends HttpServlet {
    private PracticaDAO dao = new PracticaDAO();
    private Gson gson = new Gson();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Practica> lista = dao.listarTodas();
        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(lista));
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Practica p = new Practica();
        p.setNombre(req.getParameter("nombre"));
        p.setTipoPractica(req.getParameter("tipo"));
        p.setHorasReglamentarias(Integer.parseInt(req.getParameter("horas")));
        p.setEstado("Activa");
        p.setFechaInicio(Date.valueOf(req.getParameter("inicio")));
        p.setFechaFin(Date.valueOf(req.getParameter("fin")));
        p.setSemestre(req.getParameter("semestre"));

        boolean ok = dao.crearPractica(p);
        resp.getWriter().print(ok ? "success" : "error");
    }
}