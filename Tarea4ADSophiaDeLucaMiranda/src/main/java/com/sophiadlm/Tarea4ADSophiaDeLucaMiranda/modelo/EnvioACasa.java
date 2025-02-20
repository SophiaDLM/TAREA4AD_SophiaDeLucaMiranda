package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class EnvioACasa {
    //Atributos de la clase:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double peso;
    private int volumen; //VER ESPECIFICACIONES
    private boolean urgente = false;

    //Relación entre EnvioACasa y Direccion:
    @ManyToOne
    private Direccion direccion;

    //Relación entre EnvioACasa y Parada:
    private Long idParada;

    //Constructores de la clase:
    public EnvioACasa() {

    }

    public EnvioACasa(double peso, int volumen, boolean urgente) {
        this.peso = peso;
        this.volumen = volumen;
        this.urgente = urgente;
    }

    //Getters y Setters de la clase:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getVolumen() {
        return volumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public boolean isUrgente() {
        return urgente;
    }

    public void setUrgente(boolean urgente) {
        this.urgente = urgente;
    }

    //Métodos básicos:
    @Override
    public String toString() { //EDITAR LUEGO
        return "EnvioACasa{" +
                "id=" + id +
                ", peso=" + peso +
                ", volumen=" + volumen +
                ", urgente=" + urgente +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EnvioACasa that = (EnvioACasa) o;
        return Double.compare(peso, that.peso) == 0 && volumen == that.volumen && urgente == that.urgente && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peso, volumen, urgente);
    }
}