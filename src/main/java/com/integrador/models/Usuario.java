package com.integrador.models;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private String estado;

    // Constructores
    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String email, String password, String rol, String estado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}