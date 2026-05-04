package com.proyecto.integrador.dto;

public class ObservacionRequest {
    private Long idBitacora;
    private Long idAsesor;
    private String texto;
    private java.time.LocalDate fecha;

    public ObservacionRequest() {}

    public ObservacionRequest(Long idBitacora, Long idAsesor, String texto, java.time.LocalDate fecha) {
        this.idBitacora = idBitacora;
        this.idAsesor = idAsesor;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Long getIdBitacora() { return idBitacora; }
    public void setIdBitacora(Long idBitacora) { this.idBitacora = idBitacora; }
    public Long getIdAsesor() { return idAsesor; }
    public void setIdAsesor(Long idAsesor) { this.idAsesor = idAsesor; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public java.time.LocalDate getFecha() { return fecha; }
    public void setFecha(java.time.LocalDate fecha) { this.fecha = fecha; }


}
