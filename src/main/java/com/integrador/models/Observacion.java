package com.integrador.models;
import java.sql.Date;

public class Observacion {
    private int idObservacion;
    private int idBitacora;
    private int idAsesor;
    private String texto;
    private Date fecha;

    public Observacion() {}

    // Getters y Setters
    public int getIdObservacion() { return idObservacion; }
    public void setIdObservacion(int idObservacion) { this.idObservacion = idObservacion; }
    public int getIdBitacora() { return idBitacora; }
    public void setIdBitacora(int idBitacora) { this.idBitacora = idBitacora; }
    public int getIdAsesor() { return idAsesor; }
    public void setIdAsesor(int idAsesor) { this.idAsesor = idAsesor; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}