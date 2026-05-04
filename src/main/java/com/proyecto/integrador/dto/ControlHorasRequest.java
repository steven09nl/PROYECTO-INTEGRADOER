package com.proyecto.integrador.dto;

public class ControlHorasRequest {
    private Long idBitacora;
    private java.time.LocalDate fecha;
    private java.time.LocalTime horaEntrada;
    private java.time.LocalTime horaSalida;
    private Double horasCumplidas;

    public ControlHorasRequest() {}

    public ControlHorasRequest(Long idBitacora, java.time.LocalDate fecha, java.time.LocalTime horaEntrada, java.time.LocalTime horaSalida, Double horasCumplidas) {
        this.idBitacora = idBitacora;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.horasCumplidas = horasCumplidas;
    }

    public Long getIdBitacora() { return idBitacora; }
    public void setIdBitacora(Long idBitacora) { this.idBitacora = idBitacora; }
    public java.time.LocalDate getFecha() { return fecha; }
    public void setFecha(java.time.LocalDate fecha) { this.fecha = fecha; }
    public java.time.LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(java.time.LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }
    public java.time.LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(java.time.LocalTime horaSalida) { this.horaSalida = horaSalida; }
    public Double getHorasCumplidas() { return horasCumplidas; }
    public void setHorasCumplidas(Double horasCumplidas) { this.horasCumplidas = horasCumplidas; }


}
