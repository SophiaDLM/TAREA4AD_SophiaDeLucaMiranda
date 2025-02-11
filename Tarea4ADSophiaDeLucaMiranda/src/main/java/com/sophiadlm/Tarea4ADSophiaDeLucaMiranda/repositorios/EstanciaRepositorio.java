package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Estancia;

import java.time.LocalDate;
import java.util.List;

/***
 * Interfaz EstanciaRepositorio que hereda de JpaRepository y se inyecta en EstanciaServicio.
 *
 * Contiene métodos personalizados como findByPeregrinoId y encontrarEstanciasPorParadaYRangoFecha que se
 * implementa con un @Query ya que la información que debe manejar es muy específica.
 */
@Repository
public interface EstanciaRepositorio extends JpaRepository<Estancia, Long> {
    List<Estancia> findByPeregrinoId(Long idPeregrino);

    @Query(value = "SELECT id, fecha, vip, id_parada, id_peregrino FROM estancia WHERE id_parada = :idParada AND fecha BETWEEN :fechaInicio AND :fechaFin", nativeQuery = true)
    List<Estancia> encontrarEstanciasPorParadaYRangoFecha(@Param("idParada") Long idParada, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
}