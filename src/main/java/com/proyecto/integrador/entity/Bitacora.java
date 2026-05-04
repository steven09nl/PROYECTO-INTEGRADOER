package com.proyecto.integrador.entity;

public class Bitacora {
    private Long idBitacora;
    private Long idEstudiante;
    private Long idPractica;
    private String estado;
    private String modalidad;
    private java.time.LocalDate fechaEnvio;
    private Double calificacion;

    public Bitacora() {}

    public Bitacora(Long idBitacora, Long idEstudiante, Long idPractica, String estado, String modalidad, java.time.LocalDate fechaEnvio, Double calificacion) {
        this.idBitacora = idBitacora;
        this.idEstudiante = idEstudiante;
        this.idPractica = idPractica;
        this.estado = estado;
        this.modalidad = modalidad;
        this.fechaEnvio = fechaEnvio;
        this.calificacion = calificacion;
    }

    public Long getIdBitacora() { return idBitacora; }
    public void setIdBitacora(Long idBitacora) { this.idBitacora = idBitacora; }
    public Long getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Long idEstudiante) { this.idEstudiante = idEstudiante; }
    public Long getIdPractica() { return idPractica; }
    public void setIdPractica(Long idPractica) { this.idPractica = idPractica; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public java.time.LocalDate getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(java.time.LocalDate fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }


}
