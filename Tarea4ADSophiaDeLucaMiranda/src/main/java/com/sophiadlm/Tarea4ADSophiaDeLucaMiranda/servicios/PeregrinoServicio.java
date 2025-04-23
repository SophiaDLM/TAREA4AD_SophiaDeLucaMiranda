package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Peregrino;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.PeregrinoRepositorio;

/***
 * Clase PeregrinoServicio que se encarga de gestionar las transacciones con la base de datos
 * utilizando métodos predefinidos que provienen de la interfaz PeregrinoRepositorio y que ésta, a su vez,
 * hereda los métodos de JpaRepository.
 */
@Service
public class PeregrinoServicio {
    @Autowired
    private PeregrinoRepositorio peregrinoRep;

    //Métodos predefinidos:
    public Peregrino guardar(Peregrino entity) {
        return peregrinoRep.save(entity);
    }

    public Peregrino actualizar(Peregrino entity) {
        return peregrinoRep.save(entity);
    }

    public Peregrino encontrarPorId(Long id) {
        return peregrinoRep.findById(id).get();
    }

    public List<Peregrino> encontrarTodos() {
        return peregrinoRep.findAll();
    }

    //Métodos personalizados:
    public Peregrino encontrarPorNombre(String nombre) {
        return peregrinoRep.findByNombre(nombre);
    }
}