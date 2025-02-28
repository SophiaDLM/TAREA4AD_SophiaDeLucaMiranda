package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Embeddable
public class Direccion {
    //Atributos de la clase:
    private String direccion;
    private String localidad;

    //Constructores de la clase:
    public Direccion() {

    }

    public Direccion(String direccion, String localidad) {
        this.direccion = direccion;
        this.localidad = localidad;
    }

    //Getters y Setters de la clase:
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    //Métodos básicos:
    @Override
    public String toString() {
        return "Direccion{direccion= " + direccion + ", localidad= " + localidad + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion1 = (Direccion) o;
        return Objects.equals(direccion, direccion1.direccion) && Objects.equals(localidad, direccion1.localidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direccion, localidad);
    }
}