package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.db4o.ObjectContainer;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexion;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.ConjuntoContratado;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Servicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.ConjuntoContratadoRepositorio;

import java.util.List;

public class ConjuntoContratadoServicio implements ConjuntoContratadoRepositorio {
    private ObjectContainer baseDatos = DataConexion.obtenerInstancia();

    @Override
    public ConjuntoContratado guardar(ConjuntoContratado entity) {
        baseDatos.store(entity);
        baseDatos.commit();
        return entity;
    }

    @Override
    public ConjuntoContratado actualizar(ConjuntoContratado entity) {
        ConjuntoContratado conjunto = encontrarPorId(entity.getId());

        if(conjunto != null) {
            conjunto.setPrecioTotal(entity.getPrecioTotal());
            conjunto.setModoPago(entity.getModoPago());
            conjunto.setExtra(entity.getExtra());
            baseDatos.store(conjunto);
            baseDatos.commit();
            return conjunto;
        } else {
            return null;
        }
    }

    @Override
    public void borrar(ConjuntoContratado entity) {
        baseDatos.delete(entity);
        baseDatos.commit();
    }

    @Override
    public void borrarPorId(Long id) {
        ConjuntoContratado conjunto = encontrarPorId(id);

        if(conjunto != null) {
            baseDatos.delete(conjunto);
            baseDatos.commit();
        }
    }

    @Override
    public void borrarPorLote(List<ConjuntoContratado> conjuntos) {
        for(ConjuntoContratado indice: conjuntos) {
            baseDatos.delete(indice);
        }
        baseDatos.commit();
    }

    @Override
    public ConjuntoContratado encontrarPorId(Long id) {
        List<ConjuntoContratado> conjuntos = baseDatos.query(ConjuntoContratado.class);

        for(ConjuntoContratado indice: conjuntos) {
            if(indice.getId().equals(id)) {
                return indice;
            }
        }
        return null;
    }

    @Override
    public List<ConjuntoContratado> encontrarTodos() {
        List<ConjuntoContratado> listaConjuntos = baseDatos.query(ConjuntoContratado.class);
        return listaConjuntos;
    }
}