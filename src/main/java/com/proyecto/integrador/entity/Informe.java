package com.proyecto.integrador.entity;

public class Informe {
    private Long idInforme;
    private Long idUsuarioGen;
    private String tipoInforme;
    private java.time.LocalDate fechaGeneracion;
    private String periodo;
    private String urlArchivo;

    public Informe() {}

    public Informe(Long idInforme, Long idUsuarioGen, String tipoInforme, java.time.LocalDate fechaGeneracion, String periodo, String urlArchivo) {
        this.idInforme = idInforme;
        this.idUsuarioGen = idUsuarioGen;
        this.tipoInforme = tipoInforme;
        this.fechaGeneracion = fechaGeneracion;
        this.periodo = periodo;
        this.urlArchivo = urlArchivo;
    }

    public Long getIdInforme() { return idInforme; }
    public void setIdInforme(Long idInforme) { this.idInforme = idInforme; }
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
