package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.util.Objects;

public class Servicio {
    //Atributos de la clase:
    private Long id;
    private String nombre;
    private double precio;

    //Relación con parada:
    private Long idParada;

    //Constructores de la clase:
    public Servicio() {

    }

    public Servicio(String nombre, double precio) {
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
        return Double.compare(precio, servicio.precio) == 0 && Objects.equals(id, servicio.id) && Objects.equals(nombre, servicio.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio);
    }
}