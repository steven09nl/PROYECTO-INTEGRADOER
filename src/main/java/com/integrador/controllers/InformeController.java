package com.integrador.controllers;

import com.google.gson.Gson;
import com.integrador.dao.InformeDAO;
import com.integrador.models.Informe;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/informes")
public class InformeController extends HttpServlet {

    private InformeDAO informeDAO = new InformeDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        List<Informe> lista = informeDAO.listarInformes();
        resp.getWriter().print(gson.toJson(lista));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Informe info = new Informe();
            info.setIdUsuarioGen(Integer.parseInt(req.getParameter("idUsuarioGen")));
            info.setTipoInforme(req.getParameter("tipoInforme"));
            info.setPeriodo(req.getParameter("periodo"));
            info.setUrlArchivo(req.getParameter("urlArchivo"));
            info.setFechaGeneracion(Date.valueOf(LocalDate.now())); // Se asigna la fecha actual automáticamente

            boolean exito = informeDAO.registrarInforme(info);

            if (exito) {
                resp.getWriter().print("{\"status\":\"success\", \"mensaje\":\"Informe generado y registrado exitosamente.\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"No se pudo registrar el informe en la base de datos.\"}");
            }

        } catch (Exception e) {
            resp.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Error: " + e.getMessage() + "\"}");
        }
    }
}