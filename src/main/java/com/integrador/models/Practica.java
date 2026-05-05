package com.integrador.models;
import java.sql.Date;

public class Practica {
    private int idPractica;
    private String nombre;
    private String tipoPractica;
    private int horasReglamentarias;
    private String estado;
    private Date fechaInicio;
    private Date fechaFin;
    private String semestre;

    public Practica() {}

    // Getters y Setters
    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoPractica() { return tipoPractica; }
    public void setTipoPractica(String tipoPractica) { this.tipoPractica = tipoPractica; }
    public int getHorasReglamentarias() { return horasReglamentarias; }
    public void setHorasReglamentarias(int horasReglamentarias) { this.horasReglamentarias = horasReglamentarias; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }
}