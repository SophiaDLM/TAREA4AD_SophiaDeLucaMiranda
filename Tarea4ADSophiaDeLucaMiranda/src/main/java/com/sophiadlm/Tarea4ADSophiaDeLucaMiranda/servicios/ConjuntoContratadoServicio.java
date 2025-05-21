package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.ConjuntoContratado;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.ConjuntoContratadoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConjuntoContratadoServicio {
    @Autowired
    private ConjuntoContratadoRepositorio conjuntoContratadoRep;

    public boolean guardar(ConjuntoContratado entity) {
        return conjuntoContratadoRep.guardarDB4O(entity);
    }

    public List<ConjuntoContratado> encontrarTodos() {
        return conjuntoContratadoRep.encontrarTodosDB4O();
    }
}