package com.empresa.eventos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Modelo de dominio que representa un evento.
 * Aplica encapsulamiento: todos los atributos son privados con getters/setters.
 */
public class Evento {

    private Integer idEvento;
    private String nombreEvento;
    private String tipoEvento;
    private String cliente;
    private LocalDate fechaEvento;
    private String lugar;
    private BigDecimal presupuesto;
    private String estado;

    public Evento() {
    }

    public Evento(Integer idEvento, String nombreEvento, String tipoEvento,
                  String cliente, LocalDate fechaEvento, String lugar,
                  BigDecimal presupuesto, String estado) {
        this.idEvento = idEvento;
        this.nombreEvento = nombreEvento;
        this.tipoEvento = tipoEvento;
        this.cliente = cliente;
        this.fechaEvento = fechaEvento;
        this.lugar = lugar;
        this.presupuesto = presupuesto;
        this.estado = estado;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evento)) return false;
        Evento evento = (Evento) o;
        return Objects.equals(idEvento, evento.idEvento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvento);
    }

    @Override
    public String toString() {
        return "Evento{idEvento=" + idEvento
                + ", nombreEvento='" + nombreEvento + '\''
                + ", cliente='" + cliente + '\''
                + ", estado='" + estado + '\'' + '}';
    }
}
