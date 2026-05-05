package com.integrador.controllers;

import com.google.gson.Gson;
import com.integrador.dao.ControlHorasDAO;
import com.integrador.models.ControlHoras;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/registro_horas")
public class ControlHorasController extends HttpServlet {

    private ControlHorasDAO controlDAO = new ControlHorasDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        Map<String, Object> respuesta = new HashMap<>();

        try {
            int idBitacora = Integer.parseInt(req.getParameter("idBitacora"));
            String fechaStr = req.getParameter("fecha");
            String horaEntradaStr = req.getParameter("horaEntrada");
            String horaSalidaStr = req.getParameter("horaSalida");

            // Parsear fechas y horas
            Date fecha = Date.valueOf(fechaStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime entradaDt = LocalDateTime.parse(fechaStr + "T" + horaEntradaStr, formatter);
            LocalDateTime salidaDt = LocalDateTime.parse(fechaStr + "T" + horaSalidaStr, formatter);

            // Calcular horas cumplidas (como float)
            Duration duracion = Duration.between(entradaDt, salidaDt);
            float horasCumplidas = duracion.toMinutes() / 60.0f;

            ControlHoras ch = new ControlHoras();
            ch.setIdBitacora(idBitacora);
            ch.setFecha(fecha);
            ch.setHoraEntrada(Timestamp.valueOf(entradaDt));
            ch.setHoraSalida(Timestamp.valueOf(salidaDt));
            ch.setHorasCumplidas(horasCumplidas);

            boolean exito = controlDAO.registrarHoras(ch);

            if (exito) {
                respuesta.put("status", "success");
                respuesta.put("mensaje", "Horas registradas correctamente: " + String.format("%.2f", horasCumplidas) + " horas.");
            } else {
                respuesta.put("status", "error");
                respuesta.put("mensaje", "No se pudo registrar las horas en la base de datos.");
            }

        } catch (Exception e) {
            respuesta.put("status", "error");
            respuesta.put("mensaje", "Error procesando los datos: " + e.getMessage());
        }

        out.print(gson.toJson(respuesta));
        out.flush();
    }
}