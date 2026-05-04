package com.proyecto.integrador.entity;

public class Practica {
    private Long idPractica;
    private String nombre;
    private String tipoPractica;
    private Integer horasReglamentarias;
    private String estado;
    private java.time.LocalDate fechaInicio;
    private java.time.LocalDate fechaFin;
    private String semestre;

    public Practica() {}

    public Practica(Long idPractica, String nombre, String tipoPractica, Integer horasReglamentarias, String estado, java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin, String semestre) {
        this.idPractica = idPractica;
        this.nombre = nombre;
        this.tipoPractica = tipoPractica;
        this.horasReglamentarias = horasReglamentarias;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.semestre = semestre;
    }

    public Long getIdPractica() { return idPractica; }
    public void setIdPractica(Long idPractica) { this.idPractica = idPractica; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoPractica() { return tipoPractica; }
    public void setTipoPractica(String tipoPractica) { this.tipoPractica = tipoPractica; }
    public Integer getHorasReglamentarias() { return horasReglamentarias; }
    public void setHorasReglamentarias(Integer horasReglamentarias) { this.horasReglamentarias = horasReglamentarias; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public java.time.LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(java.time.LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public java.time.LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(java.time.LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }


}
