package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Parada;

/***
 * Interfaz ParadaRepositorio que hereda de JpaRepository y se inyecta en ParadaServicio.
 */
@Repository
public interface ParadaRepositorio extends JpaRepository<Parada, Long> {
    Parada findByNombre(String nombre);
}