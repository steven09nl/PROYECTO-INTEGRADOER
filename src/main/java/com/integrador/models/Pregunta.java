package com.integrador.models;

public class Pregunta {
    private int idPregunta;
    private int idPractica;
    private String enunciado;
    private String tipoPregunta;
    private int obligatoria;
    private int orden;

    public Pregunta() {}

    public int getIdPregunta() { return idPregunta; }
    public void setIdPregunta(int idPregunta) { this.idPregunta = idPregunta; }
    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getTipoPregunta() { return tipoPregunta; }
    public void setTipoPregunta(String tipoPregunta) { this.tipoPregunta = tipoPregunta; }
    public int getObligatoria() { return obligatoria; }
    public void setObligatoria(int obligatoria) { this.obligatoria = obligatoria; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
}
