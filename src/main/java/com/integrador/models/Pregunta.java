package com.integrador.models;

public class Pregunta {
    private int idPregunta;
    private String pregunta;
    private int idPractica;

    public Pregunta() {}

    // Getters y Setters
    public int getIdPregunta() { return idPregunta; }
    public void setIdPregunta(int idPregunta) { this.idPregunta = idPregunta; }
    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }
    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }
}