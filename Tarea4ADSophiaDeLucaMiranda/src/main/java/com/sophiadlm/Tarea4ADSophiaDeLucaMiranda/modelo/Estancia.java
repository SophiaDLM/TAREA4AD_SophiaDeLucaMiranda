package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/***
 * Clase Estancia del tipo @Entity que generará una tabla en la base de datos y desde la cual se
 * podrán manejar los datos gracias al uso de las clases @Service.
 */
@Entity
@Table(name = "Estancia")
public class Estancia {
    //Atributos de la clase:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private LocalDate fecha;

    private boolean vip = false;

    //Representa la relación ManyToOne con la tabla peregrino (crea foreign keys):
    @ManyToOne
    @JoinColumn(name = "idPeregrino", nullable = false)
    private Peregrino peregrino;

    //Representa la relación ManyToOne con la tabla parada (crea foreign keys):
    @ManyToOne
    @JoinColumn(name = "idParada", nullable = false)
    private Parada parada;

    //Constructores de la clase:
    public Estancia() {

    }

    public Estancia(LocalDate fecha, boolean vip) {
        this.fecha = fecha;
        this.vip = vip;
    }

    //Getters y Setters de la clase:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public Peregrino getPeregrino() {
        return peregrino;
    }

    public void setPeregrino(Peregrino peregrino) {
        this.peregrino = peregrino;
    }

    public Parada getParada() {
        return parada;
    }

    public void setParada(Parada parada) {
        this.parada = parada;
    }

    //Métodos básicos:
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Estancia estancia = (Estancia) o;
        return vip == estancia.vip && Objects.equals(id, estancia.id) && Objects.equals(fecha, estancia.fecha) && Objects.equals(peregrino, estancia.peregrino) && Objects.equals(parada, estancia.parada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, vip, peregrino, parada);
    }
}