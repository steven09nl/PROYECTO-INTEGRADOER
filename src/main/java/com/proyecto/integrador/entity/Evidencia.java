package com.proyecto.integrador.entity;

public class Evidencia {
    private Long idEvidencia;
    private Long idBitacora;
    private String urlArchivo;
    private java.time.LocalDate fechaCarga;
    private String descripcion;

    public Evidencia() {}

    public Evidencia(Long idEvidencia, Long idBitacora, String urlArchivo, java.time.LocalDate fechaCarga, String descripcion) {
        this.idEvidencia = idEvidencia;
        this.idBitacora = idBitacora;
        this.urlArchivo = urlArchivo;
        this.fechaCarga = fechaCarga;
        this.descripcion = descripcion;
    }

    public Long getIdEvidencia() { return idEvidencia; }
    public void setIdEvidencia(Long idEvidencia) { this.idEvidencia = idEvidencia; }
    public Long getIdBitacora() { return idBitacora; }
    public void setIdBitacora(Long idBitacora) { this.idBitacora = idBitacora; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
    public java.time.LocalDate getFechaCarga() { return fechaCarga; }
    public void setFechaCarga(java.time.LocalDate fechaCarga) { this.fechaCarga = fechaCarga; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }


}
