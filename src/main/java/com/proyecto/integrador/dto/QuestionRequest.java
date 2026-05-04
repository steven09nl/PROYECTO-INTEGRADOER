package com.proyecto.integrador.dto;

public class QuestionRequest {
    private Long idPractica;
    private String enunciado;
    private String tipoPregunta;
    private Boolean obligatoria;
    private Integer orden;

    public QuestionRequest() {}

    public QuestionRequest(Long idPractica, String enunciado, String tipoPregunta, Boolean obligatoria, Integer orden) {
        this.idPractica = idPractica;
        this.enunciado = enunciado;
        this.tipoPregunta = tipoPregunta;
        this.obligatoria = obligatoria;
        this.orden = orden;
    }

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
