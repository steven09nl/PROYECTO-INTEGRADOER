package com.integrador.models;

public class OpcionPregunta {
    private int    idOpcion;
    private int    idPregunta;
    private String textoOpcion;
    private int    esCorrecta;   // 0 o 1

    public OpcionPregunta() {}

    public int    getIdOpcion()         { return idOpcion; }
    public void   setIdOpcion(int v)    { this.idOpcion = v; }
    public int    getIdPregunta()       { return idPregunta; }
    public void   setIdPregunta(int v)  { this.idPregunta = v; }
    public String getTextoOpcion()      { return textoOpcion; }
    public void   setTextoOpcion(String v){ this.textoOpcion = v; }
    public int    getEsCorrecta()       { return esCorrecta; }
    public void   setEsCorrecta(int v)  { this.esCorrecta = v; }
    @Override public String toString()  { return textoOpcion + (esCorrecta==1?" ✓":""); }
}
