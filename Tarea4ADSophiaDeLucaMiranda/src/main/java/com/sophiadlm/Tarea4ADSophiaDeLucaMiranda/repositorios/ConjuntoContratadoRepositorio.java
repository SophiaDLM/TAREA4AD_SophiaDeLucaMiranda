package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.db4o.ObjectContainer;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexion;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.ConjuntoContratado;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConjuntoContratadoRepositorio {
    private ObjectContainer baseDatos;

    public ConjuntoContratadoRepositorio() {
        this.baseDatos = DataConexion.obtenerInstanciaObjectContainer();
    }

    public boolean guardarDB4O(ConjuntoContratado entity) {
        try {
            baseDatos.store(entity);
            baseDatos.commit();
            return true;

        } catch (Exception e) {
            System.out.println(">> FATAL ERROR - No se ha podido guardar el servicio: ");
            e.printStackTrace();
        }
        return false;
    }

    public List<ConjuntoContratado> encontrarTodosDB4O() {
        List<ConjuntoContratado> listaConjuntos = baseDatos.query(ConjuntoContratado.class);
        return listaConjuntos;
    }
}