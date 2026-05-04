package com.proyecto.integrador.dto;

public class InformeRequest {
    private Long idUsuarioGen;
    private String tipoInforme;
    private java.time.LocalDate fechaGeneracion;
    private String periodo;
    private String urlArchivo;

    public InformeRequest() {}

    public InformeRequest(Long idUsuarioGen, String tipoInforme, java.time.LocalDate fechaGeneracion, String periodo, String urlArchivo) {
        this.idUsuarioGen = idUsuarioGen;
        this.tipoInforme = tipoInforme;
        this.fechaGeneracion = fechaGeneracion;
        this.periodo = periodo;
        this.urlArchivo = urlArchivo;
    }

    public Long getIdUsuarioGen() { return idUsuarioGen; }
    public void setIdUsuarioGen(Long idUsuarioGen) { this.idUsuarioGen = idUsuarioGen; }
    public String getTipoInforme() { return tipoInforme; }
    public void setTipoInforme(String tipoInforme) { this.tipoInforme = tipoInforme; }
    public java.time.LocalDate getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(java.time.LocalDate fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }


}
