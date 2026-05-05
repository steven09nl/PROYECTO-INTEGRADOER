package com.integrador.models;
import java.sql.Date;
import java.sql.Timestamp;

public class ControlHoras {
    private int idRegistro;
    private int idBitacora; // fk a Bitacora [cite: 8]
    private Date fecha; // [cite: 8]
    private Timestamp horaEntrada; // [cite: 8]
    private Timestamp horaSalida; // [cite: 8]
    private float horasCumplidas; // [cite: 8]

    public ControlHoras() {}

    // Getters y Setters
    public int getIdRegistro() { return idRegistro; }
    public void setIdRegistro(int idRegistro) { this.idRegistro = idRegistro; }
    public int getIdBitacora() { return idBitacora; }
    public void setIdBitacora(int idBitacora) { this.idBitacora = idBitacora; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Timestamp getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(Timestamp horaEntrada) { this.horaEntrada = horaEntrada; }
    public Timestamp getHoraSalida() { return horaSalida; }
    public void setHoraSalida(Timestamp horaSalida) { this.horaSalida = horaSalida; }
    public float getHorasCumplidas() { return horasCumplidas; }
    public void setHorasCumplidas(float horasCumplidas) { this.horasCumplidas = horasCumplidas; }
}