package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Parada;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.ParadaRepositorio;

/***
 * Clase ParadaServicio que se encarga de gestionar las transacciones con la base de datos
 * utilizando métodos predefinidos que provienen de la interfaz ParadaRepositorio y que ésta, a su vez,
 * hereda los métodos de JpaRepository.
 */
@Service
public class ParadaServicio {
    @Autowired
    private ParadaRepositorio paradaRep;

    //Métodos predefinidos:
    public Parada guardar(Parada entity) {
        return paradaRep.save(entity);
    }

    public Parada actualizar(Parada entity) {
        return paradaRep.save(entity);
    }

    public Parada encontrarPorId(Long id) {
        return paradaRep.findById(id).get();
    }

    public List<Parada> encontrarTodos() {
        return paradaRep.findAll();
    }

    //Métodos personalizados:
    public Parada encontrarPorNombre(String nombre) {
        return paradaRep.findByNombre(nombre);
    }
}