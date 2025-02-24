package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexionObjectDB;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.EnvioACasa;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.EnvioACasaRepositorio;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EnvioACasaServicio implements EnvioACasaRepositorio {
    private EntityManager manejadorEntidades = DataConexionObjectDB.obtenerInstancia();

    @Override
    public EnvioACasa guardar(EnvioACasa entity) {
        manejadorEntidades.getTransaction().begin();
        manejadorEntidades.persist(entity);
        manejadorEntidades.getTransaction().commit();
        return entity;
    }

    @Override
    public EnvioACasa actualizar(EnvioACasa entity) {
        manejadorEntidades.getTransaction().begin();
        EnvioACasa envio = manejadorEntidades.find(EnvioACasa.class, entity.getId());

        if(envio != null) {
            envio.setPeso(envio.getPeso());
            envio.setVolumen(envio.getVolumen());
            envio.setUrgente(envio.isUrgente());
            //envio.setDireccion(envio.getDireccion());
            manejadorEntidades.merge(envio);
        }

        manejadorEntidades.getTransaction().commit();
        return envio;
    }

    @Override
    public void borrar(EnvioACasa entity) {
        manejadorEntidades.getTransaction().begin();

        if(entity != null) {
            manejadorEntidades.remove(entity);
        }

        manejadorEntidades.getTransaction().commit();
    }

    @Override
    public void borrarPorId(Long id) {
        manejadorEntidades.getTransaction().begin();
        EnvioACasa envio = manejadorEntidades.find(EnvioACasa.class, id);

        if(envio != null) {
            manejadorEntidades.remove(envio);
        }

        manejadorEntidades.getTransaction().commit();
    }

    @Override
    public void borrarPorLote(List<EnvioACasa> envios) {
        manejadorEntidades.getTransaction().begin();

        for(EnvioACasa indice: envios) {
            if(indice != null) {
                manejadorEntidades.remove(indice);
            }
        }
    }

    @Override
    public EnvioACasa encontrarPorId(Long id) {
        return manejadorEntidades.find(EnvioACasa.class, id);
    }

    @Override
    public List<EnvioACasa> encontrarTodos() {
        return manejadorEntidades.createQuery("SELECT d FROM Direccion d", EnvioACasa.class).getResultList();
    }
}