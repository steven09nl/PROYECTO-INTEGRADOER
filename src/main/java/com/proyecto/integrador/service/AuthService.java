package com.proyecto.integrador.service;

import org.springframework.stereotype.Service;

import com.proyecto.integrador.entity.Usuario;
import com.proyecto.integrador.repository.UsuarioRepository;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario login(String email, String password) {
        return usuarioRepository.findByEmailAndPassword(email, password);
    }
}
