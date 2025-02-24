package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.util.List;
import java.util.Objects;

public class Servicio {
    //Atributos de la clase:
    private Long id;
    private static Long contador;
    private String nombre;
    private double precio;

    //Relación entre Servicio y ConjuntoContratado:
    private List<Long> idConjuntosContratados;

    //Constructores de la clase:
    public Servicio() {

    }

    public Servicio(String nombre, double precio) {
        contador++;
        this.id = contador;
        this.nombre = nombre;
        this.precio = precio;
    }

    //Getters y Setters de la clase:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public List<Long> getIdConjuntosContratadosContratado() {
        return idConjuntosContratados;
    }

    public void setIdConjuntosContratadosContratado(List<Long> idConjuntoContratado) {
        this.idConjuntosContratados = idConjuntosContratados;
    }

    //Métodos básicos:
    @Override
    public String toString() { //EDITAR LUEGO
        return "Servicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servicio servicio = (Servicio) o;
        return Double.compare(precio, servicio.precio) == 0 && Objects.equals(id, servicio.id) && Objects.equals(nombre, servicio.nombre) && Objects.equals(idConjuntosContratados, servicio.idConjuntosContratados);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, idConjuntosContratados);
    }
}