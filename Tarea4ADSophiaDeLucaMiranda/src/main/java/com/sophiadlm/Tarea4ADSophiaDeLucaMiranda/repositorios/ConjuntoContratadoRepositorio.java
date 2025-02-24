package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.ConjuntoContratado;

import java.util.List;

public interface ConjuntoContratadoRepositorio {
    ConjuntoContratado guardar(ConjuntoContratado entity);

    ConjuntoContratado actualizar(ConjuntoContratado entity);

    void borrar(ConjuntoContratado entity);

    void borrarPorId(Long id);

    void borrarPorLote(List<ConjuntoContratado> conjuntoContratados);

    ConjuntoContratado encontrarPorId(Long id);

    List<ConjuntoContratado> encontrarTodos();
}