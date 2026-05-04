package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.LoginRequest;
import com.proyecto.integrador.entity.Usuario;
import com.proyecto.integrador.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.login(request.getEmail(), request.getPassword());

        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        if (usuario.getEstado() == null || !usuario.getEstado().equalsIgnoreCase("ACTIVO")) {
            return ResponseEntity.status(403).body("Usuario inactivo");
        }

        return ResponseEntity.ok(usuario);
    }
}
