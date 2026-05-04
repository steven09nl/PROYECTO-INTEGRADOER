package com.proyecto.integrador.dto;

public class RespuestaRequest {
    private Long idPregunta;
    private Long idBitacora;
    private String textoRespuesta;
    private java.time.LocalDate fechaRespuesta;
    private String retroalimentacion;

    public RespuestaRequest() {}

    public RespuestaRequest(Long idPregunta, Long idBitacora, String textoRespuesta, java.time.LocalDate fechaRespuesta, String retroalimentacion) {
        this.idPregunta = idPregunta;
        this.idBitacora = idBitacora;
        this.textoRespuesta = textoRespuesta;
        this.fechaRespuesta = fechaRespuesta;
        this.retroalimentacion = retroalimentacion;
    }

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
