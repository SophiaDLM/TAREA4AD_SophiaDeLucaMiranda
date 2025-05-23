package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Peregrino;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/***
 * Interfaz PeregrinoParadaRepositorio que hereda de JpaRepository y se inyecta en PeregrinoParadaServicio.
 *
 * Tiene consultas específicas anotadas con @Query para poder manipular la información de la base de datos.
 * En este caso es así porque la tabla peregrino_parada se genera automáticamente y no hay una clase Entity que
 * la pueda gestionar como en otros casos.
 */
@Repository
public interface PeregrinoParadaRepositorio extends JpaRepository<Peregrino, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO peregrino_parada (id_peregrino, id_parada, fecha_hora) VALUES (:idPeregrino, :idParada, :fechaHora)", nativeQuery = true)
    void insertarPeregrinoParada(@Param("idPeregrino") Long idPeregrino, @Param("idParada") Long idParada, @Param("fechaHora") LocalDateTime fechaHora);

    @Query(value = "SELECT id_parada FROM peregrino_parada WHERE id_peregrino = :idPeregrino", nativeQuery = true)
    List<Long> encontrarParadasPeregrino(@Param("idPeregrino") Long idPeregrino);

    @Query(value = "SELECT COUNT(*) FROM peregrino_parada WHERE id_peregrino = :idPeregrino AND id_parada = :idParada AND fecha_hora = :fechaHora", nativeQuery = true)
    int existePeregrinoParada(@Param("idPeregrino") Long idPeregrino, @Param("idParada") Long idParada, @Param("fechaHora")LocalDateTime fechaHora);
}