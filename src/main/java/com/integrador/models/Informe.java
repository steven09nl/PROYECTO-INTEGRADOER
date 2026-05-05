package com.integrador.models;
import java.sql.Date;

public class Informe {
    private int idInforme;
    private int idUsuarioGen; // Quién genera el informe (Director/Asesor)
    private String tipoInforme; // Ej: "Final", "Parcial"
    private Date fechaGeneracion;
    private String periodo; // Ej: "2024-1"
    private String urlArchivo; // Enlace al PDF o documento generado

    public Informe() {}

    // Getters y Setters
    public int getIdInforme() { return idInforme; }
    public void setIdInforme(int idInforme) { this.idInforme = idInforme; }
    public int getIdUsuarioGen() { return idUsuarioGen; }
    public void setIdUsuarioGen(int idUsuarioGen) { this.idUsuarioGen = idUsuarioGen; }
    public String getTipoInforme() { return tipoInforme; }
    public void setTipoInforme(String tipoInforme) { this.tipoInforme = tipoInforme; }
    public Date getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Date fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
}