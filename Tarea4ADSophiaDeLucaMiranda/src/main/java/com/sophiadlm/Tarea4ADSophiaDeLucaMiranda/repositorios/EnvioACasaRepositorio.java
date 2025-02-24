package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.EnvioACasa;

import java.util.List;

public interface EnvioACasaRepositorio {
    EnvioACasa guardar(EnvioACasa entity);

    EnvioACasa actualizar(EnvioACasa entity);

    void borrar(EnvioACasa entity);

    void borrarPorId(Long id);

    void borrarPorLote(List<EnvioACasa> envios);

    EnvioACasa encontrarPorId(Long id);

    List<EnvioACasa> encontrarTodos();
}