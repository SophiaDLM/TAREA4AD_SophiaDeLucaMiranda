package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PeregrinoParada")
public class PeregrinoParada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_peregrino_parada_id"))
    private Peregrino peregrino;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_parada_peregrino_id"))
    private Parada parada;

    //CAMBIAR A STRING NADA MÁS PARA QUE EL FORMATO SE VEA MEJOR - MODIFICAR: REPOSITORIO, SERVICIO, PARADA CONTROLADOR E INICIAR SESIÓN CONTROLADOR
    private LocalDateTime fechaHora;

    public PeregrinoParada() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    //Métodos básicos:
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PeregrinoParada that = (PeregrinoParada) o;
        return Objects.equals(id, that.id) && Objects.equals(peregrino, that.peregrino) && Objects.equals(parada, that.parada) && Objects.equals(fechaHora, that.fechaHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peregrino, parada, fechaHora);
    }
}