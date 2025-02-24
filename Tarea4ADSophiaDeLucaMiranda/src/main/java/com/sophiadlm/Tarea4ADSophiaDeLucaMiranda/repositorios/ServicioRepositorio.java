package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Servicio;

import java.util.List;

//MODIFICAR

public interface ServicioRepositorio {
    Servicio guardar(Servicio entity);

    Servicio actualizar(Servicio entity);

    void borrar(Servicio entity);

    void borrarPorId(Long id);

    void borrarPorLote(List<Servicio> servicios);

    Servicio encontrarPorId(Long id);

    List<Servicio> encontrarTodos();

    Servicio encontrarPorNombre(String nombre);
}