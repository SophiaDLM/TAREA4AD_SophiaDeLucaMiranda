package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

/***
 * Clase Carnet del tipo @Entity que generará una tabla en la base de datos y desde la cuál se
 * podrán manejar los datos gracias al uso de las clases @Service.
 */
@Entity
@Table(name = "Carnet")
public class Carnet {
    //Atributos de la clase:
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private LocalDate fechaexp;

    private double distancia;

    private int nvips;

    //Representa la relación OneToOne con la tabla peregrino:
    @OneToOne
    @PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_carnet_peregrino"))
    private Peregrino peregrino;

    //Representa la relación ManyToOne con la tabla parada para establecer la parada inicial:
    @ManyToOne
    @JoinColumn(name = "idParadaInicial", nullable = false)
    private Parada paradaInicial;

    //Constructores de la clase:
    public Carnet() {

    }

    public Carnet(Long id) {
        this.id = id;
        this.fechaexp = LocalDate.now();
        this.distancia = 0.0;
        this.nvips = 0;
    }

    //Getters y Setters de la clase:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaexp() {
        return fechaexp;
    }

    public void setFechaexp(LocalDate fechaexp) {
        this.fechaexp = fechaexp;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getNvips() {
        return nvips;
    }

    public void setNvips(int nvips) {
        this.nvips = nvips;
    }

    public Peregrino getPeregrino() {
        return peregrino;
    }

    public void setPeregrino(Peregrino peregrino) {
        this.peregrino = peregrino;
    }

    public Parada getParadaInicial() {
        return paradaInicial;
    }

    public void setParadaInicial(Parada paradaInicial) {
        this.paradaInicial = paradaInicial;
    }

    //Métodos básicos:
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Carnet carnet = (Carnet) o;
        return Double.compare(distancia, carnet.distancia) == 0 && nvips == carnet.nvips && Objects.equals(id, carnet.id) && Objects.equals(fechaexp, carnet.fechaexp) && Objects.equals(peregrino, carnet.peregrino) && Objects.equals(paradaInicial, carnet.paradaInicial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaexp, distancia, nvips, peregrino, paradaInicial);
    }
}