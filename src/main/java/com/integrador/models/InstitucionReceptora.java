package com.integrador.models;

import java.sql.Date;

/**
 * Entidad que representa una Institución Receptora de estudiantes en práctica.
 * RF Alcance 1.2 / Definición 1.3 del SRS.
 */
public class InstitucionReceptora {
    private int    idInstitucion;
    private String nombre;
    private String nit;
    private String sector;           // Público / Privado / Mixto
    private String direccion;
    private String ciudad;
    private String telefono;
    private String emailContacto;
    private String nombreCoordinador;
    private String cargoCoordinador;
    private String estado;           // ACTIVO / INACTIVO
    private Date   fechaReg;

    public InstitucionReceptora() {}

    public int    getIdInstitucion()              { return idInstitucion; }
    public void   setIdInstitucion(int v)         { this.idInstitucion = v; }
    public String getNombre()                     { return nombre; }
    public void   setNombre(String v)             { this.nombre = v; }
    public String getNit()                        { return nit; }
    public void   setNit(String v)                { this.nit = v; }
    public String getSector()                     { return sector; }
    public void   setSector(String v)             { this.sector = v; }
    public String getDireccion()                  { return direccion; }
    public void   setDireccion(String v)          { this.direccion = v; }
    public String getCiudad()                     { return ciudad; }
    public void   setCiudad(String v)             { this.ciudad = v; }
    public String getTelefono()                   { return telefono; }
    public void   setTelefono(String v)           { this.telefono = v; }
    public String getEmailContacto()              { return emailContacto; }
    public void   setEmailContacto(String v)      { this.emailContacto = v; }
    public String getNombreCoordinador()          { return nombreCoordinador; }
    public void   setNombreCoordinador(String v)  { this.nombreCoordinador = v; }
    public String getCargoCoordinador()           { return cargoCoordinador; }
    public void   setCargoCoordinador(String v)   { this.cargoCoordinador = v; }
    public String getEstado()                     { return estado; }
    public void   setEstado(String v)             { this.estado = v; }
    public Date   getFechaReg()                   { return fechaReg; }
    public void   setFechaReg(Date v)             { this.fechaReg = v; }

    @Override
    public String toString() { return idInstitucion + " – " + nombre; }
}
