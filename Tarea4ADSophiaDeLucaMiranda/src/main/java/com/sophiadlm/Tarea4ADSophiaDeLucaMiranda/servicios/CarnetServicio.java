package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Carnet;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.CarnetRepositorio;

/***
 * Clase CarnetServicio que se encarga de gestionar las transacciones con la base de datos
 * utilizando métodos predefinidos que provienen de la interfaz CarnetRepositorio y que ésta, a su vez,
 * hereda los métodos de JpaRepository.
 */
@Service
public class CarnetServicio {
    @Autowired
    private CarnetRepositorio carnetRep;

    //Métodos predefinidos:
    public Carnet guardar(Carnet entity) {
        return carnetRep.save(entity);
    }

    public Carnet actualizar(Carnet entity) {
        return carnetRep.save(entity);
    }

    public void borrar(Carnet entity ) {
        carnetRep.delete(entity);
    }

    public void borrarPorId(Long id) {
        carnetRep.deleteById(id);
    }

    public void borrarPorLote(List<Carnet> carnets) {
        carnetRep.deleteAll(carnets);
    }

    public Carnet encontrarPorId(Long id) {
        return carnetRep.findById(id).get();
    }

    public List<Carnet> encontrarTodos() {
        return carnetRep.findAll();
    }
}