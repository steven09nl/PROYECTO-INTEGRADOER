package com.proyecto.integrador.entity;

public class Respuesta {
    private Long idRespuesta;
    private Long idPregunta;
    private Long idBitacora;
    private String textoRespuesta;
    private java.time.LocalDate fechaRespuesta;
    private String retroalimentacion;

    public Respuesta() {}

    public Respuesta(Long idRespuesta, Long idPregunta, Long idBitacora, String textoRespuesta, java.time.LocalDate fechaRespuesta, String retroalimentacion) {
        this.idRespuesta = idRespuesta;
        this.idPregunta = idPregunta;
        this.idBitacora = idBitacora;
        this.textoRespuesta = textoRespuesta;
        this.fechaRespuesta = fechaRespuesta;
        this.retroalimentacion = retroalimentacion;
    }

    public Long getIdRespuesta() { return idRespuesta; }
    public void setIdRespuesta(Long idRespuesta) { this.idRespuesta = idRespuesta; }
    public Long getIdPregunta() { return idPregunta; }
    public void setIdPregunta(Long idPregunta) { this.idPregunta = idPregunta; }
    public Long getIdBitacora() { return idBitacora; }
    public void setIdBitacora(Long idBitacora) { this.idBitacora = idBitacora; }
    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }
    public java.time.LocalDate getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(java.time.LocalDate fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }


}
