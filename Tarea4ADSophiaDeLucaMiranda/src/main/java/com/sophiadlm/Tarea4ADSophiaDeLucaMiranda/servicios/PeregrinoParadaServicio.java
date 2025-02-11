package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Parada;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.ParadaRepositorio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.PeregrinoParadaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * Clase PeregrinoParadaServicio que se encarga de gestionar las transacciones con la base de datos
 * utilizando métodos personalizados que provienen de la interfaz PeregrinoParadaRepositorio y que ésta,
 * a su vez, hereda los métodos de JpaRepository.
 *
 * Es importante apuntar que los métodos de la interfaz se hicieron con anotaciones @Query, por lo que no
 * se usó ningún método predefinido.
 */
@Service
public class PeregrinoParadaServicio {
    @Autowired
    private PeregrinoParadaRepositorio peregrinoParadaRepositorio;

    @Autowired
    private ParadaRepositorio paradaRepositorio;

    //Métodos personalizados:
    public void guardarPeregrinoParada(Long idPeregrino, Long idParada) {
        peregrinoParadaRepositorio.insertarPeregrinoParada(idPeregrino, idParada);
    }

    public List<Parada> obtenerParadaPeregrino(Long idPeregrino) {
        List<Long> idParadas = peregrinoParadaRepositorio.encontrarParadasPeregrino(idPeregrino);
        return paradaRepositorio.findAllById(idParadas);
    }
}