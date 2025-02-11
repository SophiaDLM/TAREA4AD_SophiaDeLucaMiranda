package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Credenciales;

/***
 * Interfaz CredencialesRepositorio que hereda de JpaRepository y se inyecta en CredencialesServicio.
 *
 * Contiene el método personalizado findByNombreUsuario que no necesita @Query puesto que las palabras
 * reservadas findBy en Spring equivalen a una consulta del tipo SELECT, y NombreUsuario es el nombre de
 * la columna a buscar.
 */
@Repository
public interface CredencialesRepositorio extends JpaRepository<Credenciales, Long> {
    Credenciales findByNombreUsuario(String nombre);
}