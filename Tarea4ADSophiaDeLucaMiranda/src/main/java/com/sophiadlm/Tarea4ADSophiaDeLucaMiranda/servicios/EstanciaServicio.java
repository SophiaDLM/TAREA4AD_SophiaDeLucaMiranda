package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Estancia;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.EstanciaRepositorio;

/***
 * Clase EstanciaServicio que se encarga de gestionar las transacciones con la base de datos
 * utilizando métodos predefinidos y personalizados que provienen de la interfaz EstanciaRepositorio
 * y que ésta, a su vez, hereda los métodos de JpaRepository.
 *
 * Es importante apuntar que la interfaz utilizó métodos anotados con @Query.
 */
@Service
public class EstanciaServicio {
    @Autowired
    private EstanciaRepositorio estanciaRep;

    //Métodos predefinidos:
    public Estancia guardar(Estancia entity) {
        return estanciaRep.save(entity);
    }

    public Estancia actualizar(Estancia entity) {
        return estanciaRep.save(entity);
    }

    public void borrar(Estancia entity ) {
        estanciaRep.delete(entity);
    }

    public void borrarPorId(Long id) {
        estanciaRep.deleteById(id);
    }

    public void borrarPorLote(List<Estancia> estancias) {
        estanciaRep.deleteAll(estancias);
    }

    public Estancia encontrarPorId(Long id) {
        return estanciaRep.findById(id).get();
    }

    public List<Estancia> encontrarTodos() {
        return estanciaRep.findAll();
    }

    //Métodos personalizados:
    public List<Estancia> encontrarPorIdPeregrino(Long idPeregrino) {
        return estanciaRep.findByPeregrinoId(idPeregrino);
    }

    public List<Estancia> encontrarPorIdParadaYRangoFecha(Long idParada, LocalDate fechaInicio, LocalDate fechaFin) {
        return estanciaRep.encontrarEstanciasPorParadaYRangoFecha(idParada, fechaInicio, fechaFin);
    }
}