package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Peregrino;

/***
 * Interfaz PeregrinoRepositorio que hereda de JpaRepository y se inyecta en PeregrinoServicio.
 */
@Repository
public interface PeregrinoRepositorio extends JpaRepository<Peregrino, Long> {

}