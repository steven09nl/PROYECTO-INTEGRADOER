package com.integrador.models;
import java.sql.Date;

public class Respuesta {
    private int idRespuesta;
    private int idPregunta;
    private int idBitacora;
    private String textoRespuesta;
    private Date fechaRespuesta;
    private String retroalimentacion;

    public Respuesta() {}

    // Getters y Setters
    public int getIdRespuesta() { return idRespuesta; }
    public void setIdRespuesta(int idRespuesta) { this.idRespuesta = idRespuesta; }
    public int getIdPregunta() { return idPregunta; }
    public void setIdPregunta(int idPregunta) { this.idPregunta = idPregunta; }
    public int getIdBitacora() { return idBitacora; }
    public void setIdBitacora(int idBitacora) { this.idBitacora = idBitacora; }
    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }
    public Date getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(Date fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }
}