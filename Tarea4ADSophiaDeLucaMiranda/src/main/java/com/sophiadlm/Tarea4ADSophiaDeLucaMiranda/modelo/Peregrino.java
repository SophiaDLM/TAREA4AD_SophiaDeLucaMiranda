package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/***
 * Clase Peregrino del tipo @Entity que generará una tabla en la base de datos y desde la cuál se
 * podrán manejar los datos gracias al uso de las clases @Service.
 */
@Entity
@Table(name = "Peregrino")
public class Peregrino {
    //Atributos de la clase:
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String nombre;

    private String nacionalidad;

    //Representa la relación OneToOne con la tabla credenciales:
    @OneToOne
    @PrimaryKeyJoinColumn
    private Credenciales credenciales;

    //Representa la relación OneToOne con la tabla carnet:
    @OneToOne(mappedBy = "peregrino", cascade = CascadeType.ALL)
    private Carnet carnet;

    //Representación de la relación OneToMany con la tabla estancia:
    @OneToMany(mappedBy = "peregrino", cascade = CascadeType.ALL)
    private List<Estancia> listaEstancias = new ArrayList<>();

    //Representación de la relación ManyToOne con la tabla parada (genera una tabla nueva en la base de datos):
    @ManyToMany
    @JoinTable(name = "PeregrinoParada", joinColumns = @JoinColumn(name = "idPeregrino"), inverseJoinColumns = @JoinColumn(name = "idParada"))
    private List<Parada> listaParadas = new ArrayList<>();

    //Constructores de la clase:
    public Peregrino() {

    }

    public Peregrino(Long id, String nombre, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
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

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Carnet getCarnet() {
        return carnet;
    }

    public void setCarnet(Carnet carnet) {
        this.carnet = carnet;
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

    public List<Parada> getListaParadas() {
        return listaParadas;
    }

    public void setListaParadas(List<Parada> listaParadas) {
        this.listaParadas = listaParadas;
    }

    //Métodos básicos:
    @Override
    public String toString() {
        return "Peregrino [id=" + id + ", nombre=" + nombre + ", nacionalidad=" + nacionalidad + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Peregrino peregrino = (Peregrino) o;
        return Objects.equals(id, peregrino.id) && Objects.equals(nombre, peregrino.nombre) && Objects.equals(nacionalidad, peregrino.nacionalidad) && Objects.equals(credenciales, peregrino.credenciales) && Objects.equals(carnet, peregrino.carnet) && Objects.equals(listaEstancias, peregrino.listaEstancias) && Objects.equals(listaParadas, peregrino.listaParadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, nacionalidad, credenciales, carnet, listaEstancias, listaParadas);
    }
}