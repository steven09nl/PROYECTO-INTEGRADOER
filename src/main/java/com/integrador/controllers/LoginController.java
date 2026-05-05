package com.integrador.controllers;

import com.google.gson.Gson;
import com.integrador.dao.UsuarioDAO;
import com.integrador.models.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
public class LoginController extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Usuario usuario = usuarioDAO.loginEmail(email, password);
        Map<String, Object> respuesta = new HashMap<>();

        if (usuario != null) {
            // Credenciales válidas
            if(usuario.getEstado().equalsIgnoreCase("activo")) {
                 respuesta.put("status", "success");
                 respuesta.put("rol", usuario.getRol());
                 respuesta.put("mensaje", "Bienvenido " + usuario.getNombre());
            } else {
                 respuesta.put("status", "error");
                 respuesta.put("mensaje", "El usuario se encuentra inactivo");
            }
        } else {
            // Credenciales inválidas (Notificar error de acceso según el diagrama)
            respuesta.put("status", "error");
            respuesta.put("mensaje", "Credenciales inválidas");
        }

        out.print(gson.toJson(respuesta));
        out.flush();
    }
}