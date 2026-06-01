package com.integrador.models;

import java.sql.Date;

/**
 * Convenio institucional entre la UDI y una Institución Receptora.
 * RF-05 / Gestión de convenios institucionales.
 */
public class Convenio {
    private int    idConvenio;
    private int    idInstitucion;
    private String nombreInstitucion; // join display
    private String tipoConvenio;      // Marco / Específico / Pasantía
    private Date   fechaInicio;
    private Date   fechaVencimiento;
    private String estado;            // VIGENTE / VENCIDO / SUSPENDIDO
    private String descripcion;
    private Date   fechaReg;

    public Convenio() {}

    public int    getIdConvenio()              { return idConvenio; }
    public void   setIdConvenio(int v)         { this.idConvenio = v; }
    public int    getIdInstitucion()           { return idInstitucion; }
    public void   setIdInstitucion(int v)      { this.idInstitucion = v; }
    public String getNombreInstitucion()       { return nombreInstitucion; }
    public void   setNombreInstitucion(String v){ this.nombreInstitucion = v; }
    public String getTipoConvenio()            { return tipoConvenio; }
    public void   setTipoConvenio(String v)    { this.tipoConvenio = v; }
    public Date   getFechaInicio()             { return fechaInicio; }
    public void   setFechaInicio(Date v)       { this.fechaInicio = v; }
    public Date   getFechaVencimiento()        { return fechaVencimiento; }
    public void   setFechaVencimiento(Date v)  { this.fechaVencimiento = v; }
    public String getEstado()                  { return estado; }
    public void   setEstado(String v)          { this.estado = v; }
    public String getDescripcion()             { return descripcion; }
    public void   setDescripcion(String v)     { this.descripcion = v; }
    public Date   getFechaReg()                { return fechaReg; }
    public void   setFechaReg(Date v)          { this.fechaReg = v; }
}
