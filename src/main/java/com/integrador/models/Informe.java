package com.integrador.models;
import java.sql.Date;

public class Informe {
    private int    idInforme;
    private int    idUsuarioGen;
    private String tipoInforme;
    private Date   fechaGeneracion;
    private String periodo;
    private String urlArchivo;

    public Informe() {}

    public int    getIdInforme()               { return idInforme; }
    public void   setIdInforme(int v)          { this.idInforme = v; }
    public int    getIdUsuarioGen()            { return idUsuarioGen; }
    public void   setIdUsuarioGen(int v)       { this.idUsuarioGen = v; }
    public String getTipoInforme()             { return tipoInforme; }
    public void   setTipoInforme(String v)     { this.tipoInforme = v; }
    public Date   getFechaGeneracion()         { return fechaGeneracion; }
    public void   setFechaGeneracion(Date v)   { this.fechaGeneracion = v; }
    public String getPeriodo()                 { return periodo; }
    public void   setPeriodo(String v)         { this.periodo = v; }
    public String getUrlArchivo()              { return urlArchivo; }
    public void   setUrlArchivo(String v)      { this.urlArchivo = v; }
}
