package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/***
 * Clase Parada del tipo @Entity que generará una tabla en la base de datos y desde la cuál se
 * podrán manejar los datos gracias al uso de las clases @Service.
 */
@Entity
@Table(name = "Parada")
public class Parada {
    //Atributos de la clase:
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String nombre;

    private char region;

    private String responsable;

    //Representa la relación OneToOne con la tabla credenciales:
    @OneToOne
    @PrimaryKeyJoinColumn
    private Credenciales credenciales;

    //Representación de la relación OneToMany con la tabla estancia:
    @OneToMany(mappedBy = "parada", cascade = CascadeType.ALL)
    private List<Estancia> listaEstancias = new ArrayList<>();

    //Representación de la relación ManyToOne con la tabla peregrino (genera una tabla nueva en la base de datos):
    @ManyToMany(mappedBy = "listaParadas")
    private List<Peregrino> listaPeregrinos = new ArrayList<>();

    //Relación entre Parada y Servicio:
    private String idServicios;

    //Constructores de la clase:
    public Parada() {

    }

    public Parada(Long id, String nombre, char region, String responsable) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
        this.responsable = responsable;
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

    public char getRegion() {
        return region;
    }

    public void setRegion(char region) {
        this.region = region;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public List<Estancia> getListaEstancias() {
        return listaEstancias;
    }

    public void setListaEstancias(List<Estancia> listaEstancias) {
        this.listaEstancias = listaEstancias;
    }

    public List<Peregrino> getListaPeregrinos() {
        return listaPeregrinos;
    }

    public void setListaPeregrinos(List<Peregrino> listaPeregrinos) {
        this.listaPeregrinos = listaPeregrinos;
    }

    public String getIdServicios() {
        return idServicios;
    }

    public void setIdServicios(String idServicios) {
        this.idServicios = idServicios;
    }

    //Métodos básicos:
    @Override
    public String toString() {
        return "Parada [id=" + id + ", nombre=" + nombre + ", region=" + region + ", responsable=" + responsable + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Parada parada = (Parada) o;
        return region == parada.region && Objects.equals(id, parada.id) && Objects.equals(nombre, parada.nombre) && Objects.equals(responsable, parada.responsable) && Objects.equals(credenciales, parada.credenciales) && Objects.equals(listaEstancias, parada.listaEstancias) && Objects.equals(listaPeregrinos, parada.listaPeregrinos) && Objects.equals(idServicios, parada.idServicios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, region, responsable, credenciales, listaEstancias, listaPeregrinos, idServicios);
    }
}