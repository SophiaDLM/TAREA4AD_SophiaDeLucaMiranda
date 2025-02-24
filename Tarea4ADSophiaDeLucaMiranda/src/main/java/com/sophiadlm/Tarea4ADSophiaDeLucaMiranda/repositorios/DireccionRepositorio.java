package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Direccion;

import java.util.List;

public interface DireccionRepositorio {
    Direccion guardar(Direccion entity);

    Direccion actualizar(Direccion entity);

    void borrar(Direccion entity);

    void borrarPorId(Long id);

    void borrarPorLote(List<Direccion> direcciones);

    Direccion encontrarPorId(Long id);

    List<Direccion> encontrarTodos();
}