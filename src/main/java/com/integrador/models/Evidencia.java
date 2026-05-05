package com.integrador.models;
import java.sql.Date;

public class Evidencia {
    private int idEvidencias;
    private int idBitacora;
    private String urlArchivo;
    private Date fechaCarga;
    private String descripcion;

    public Evidencia() {}

    // Getters y Setters
    public int getIdEvidencias() { return idEvidencias; }
    public void setIdEvidencias(int idEvidencias) { this.idEvidencias = idEvidencias; }
    public int getIdBitacora() { return idBitacora; }
    public void setIdBitacora(int idBitacora) { this.idBitacora = idBitacora; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
    public Date getFechaCarga() { return fechaCarga; }
    public void setFechaCarga(Date fechaCarga) { this.fechaCarga = fechaCarga; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}