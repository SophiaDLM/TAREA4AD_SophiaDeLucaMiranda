package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.EnvioACasa;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.EnvioACasaRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioACasaServicio {
    private final EnvioACasaRepositorio envioRep = new EnvioACasaRepositorio();

    public boolean guardar(EnvioACasa entity) {
        return envioRep.guardarObjectDB(entity);
    }

    public List<EnvioACasa> encontrarTodos() {
        return envioRep.encontrarTodosObjectDB();
    }

    public List<EnvioACasa> encontrarTodosPorParada(Long idParada) {
        return envioRep.encontrarEnviosPorParada(idParada);
    }
}