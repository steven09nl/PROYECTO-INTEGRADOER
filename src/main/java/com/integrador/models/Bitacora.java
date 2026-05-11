package com.integrador.models;
import java.sql.Date;

public class Bitacora {
    private int idBitacora;
    private int idEstudiante; // fk a Usuario [cite: 3]
    private int idPractica;   // fk a Practica [cite: 3]
    private String estado;    // [cite: 3]
    private String modalidad; // [cite: 3]
    private Date fechaEnvio;  // [cite: 3]
    private Double calificacion; // [cite: 3]

    // Constructores, Getters y Setters
    public Bitacora() {}

    public int getIdBitacora() { return idBitacora; }
    public void setIdBitacora(int idBitacora) { this.idBitacora = idBitacora; }
    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }
    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public Date getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Date fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }
}