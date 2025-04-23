package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Servicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.ServicioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioServicio {
    @Autowired
    private ServicioRepositorio servicioRep;

    public boolean guardar(Servicio entity) {
        return servicioRep.guardarDB4O(entity);
    }

    public boolean actualizar(Servicio entity) {
        return servicioRep.actualizarDB4O(entity);
    }

    public Servicio encontrarPorId(Long id) {
        return servicioRep.encontrarPorIdDB4O(id);
    }

    public List<Servicio> encontrarTodos() {
        return servicioRep.encontrarTodosDB4O();
    }

    public Servicio encontrarPorNombre(String nombre) {
        return servicioRep.encontrarPorNombreDB4O(nombre);
    }
}