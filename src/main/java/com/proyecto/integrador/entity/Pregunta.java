package com.proyecto.integrador.entity;

public class Pregunta {
    private Long idPregunta;
    private Long idPractica;
    private String enunciado;
    private String tipoPregunta;
    private Boolean obligatoria;
    private Integer orden;

    public Pregunta() {}

    public Pregunta(Long idPregunta, Long idPractica, String enunciado, String tipoPregunta, Boolean obligatoria, Integer orden) {
        this.idPregunta = idPregunta;
        this.idPractica = idPractica;
        this.enunciado = enunciado;
        this.tipoPregunta = tipoPregunta;
        this.obligatoria = obligatoria;
        this.orden = orden;
    }

    public Long getIdPregunta() { return idPregunta; }
    public void setIdPregunta(Long idPregunta) { this.idPregunta = idPregunta; }
    public Long getIdPractica() { return idPractica; }
    public void setIdPractica(Long idPractica) { this.idPractica = idPractica; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getTipoPregunta() { return tipoPregunta; }
    public void setTipoPregunta(String tipoPregunta) { this.tipoPregunta = tipoPregunta; }
    public Boolean getObligatoria() { return obligatoria; }
    public void setObligatoria(Boolean obligatoria) { this.obligatoria = obligatoria; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }


}
