package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.util.List;
import java.util.Objects;

//TRABAJA CON BD EMBEBIDA
public class ConjuntoContratado {
    //Atributos de la clase:
    private Long id;
    private double precioTotal;
    private char modoPago;
    private String extra = null;

    //Relación entre ConjuntoContratado y Estancia
    private Long idEstancia;

    //Relación entre ConjuntoContratado y Servicio:
    private List<Long> idServicios;

    //Constructores de la clase:
    public ConjuntoContratado() {

    }

    public ConjuntoContratado(Long id, double precioTotal, char modoPago, String extra) {
        this.id = id;
        this.precioTotal = precioTotal;
        this.modoPago = modoPago;
        this.extra = extra;
    }

    //Getters y Setters de la clase:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public char getModoPago() {
        return modoPago;
    }

    public void setModoPago(char modoPago) {
        this.modoPago = modoPago;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getIdEstancia() {
        return idEstancia;
    }

    public void setIdEstancia(Long idEstancia) {
        this.idEstancia = idEstancia;
    }

    public List<Long> getIdServicios() {
        return idServicios;
    }

    public void setIdServicios(List<Long> idServicios) {
        this.idServicios = idServicios;
    }

    //Métodos básicos:
    @Override
    public String toString() { //EDITAR LUEGO
        return "ConjuntoContratado{" +
                "id=" + id +
                ", precioTotal=" + precioTotal +
                ", modoPago=" + modoPago +
                ", extra='" + extra + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConjuntoContratado that = (ConjuntoContratado) o;
        return Double.compare(precioTotal, that.precioTotal) == 0 && modoPago == that.modoPago && Objects.equals(id, that.id) && Objects.equals(extra, that.extra) && Objects.equals(idEstancia, that.idEstancia) && Objects.equals(idServicios, that.idServicios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, precioTotal, modoPago, extra, idEstancia, idServicios);
    }
}