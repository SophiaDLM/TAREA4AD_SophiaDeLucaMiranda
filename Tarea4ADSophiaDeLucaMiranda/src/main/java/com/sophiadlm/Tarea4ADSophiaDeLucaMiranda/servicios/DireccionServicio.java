package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexionObjectDB;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Direccion;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.DireccionRepositorio;
import jakarta.persistence.EntityManager;

import java.util.List;

public class DireccionServicio implements DireccionRepositorio {
    private EntityManager manejadorEntidades = DataConexionObjectDB.obtenerInstancia();

    @Override
    public Direccion guardar(Direccion entity) {
        manejadorEntidades.getTransaction().begin();
        manejadorEntidades.persist(entity);
        manejadorEntidades.getTransaction().commit();
        return entity;
    }

    @Override
    public Direccion actualizar(Direccion entity) {
        manejadorEntidades.getTransaction().begin();
        Direccion direccion = manejadorEntidades.find(Direccion.class, entity);

        if(direccion != null) {
            direccion.setDireccion(direccion.getDireccion());
            direccion.setLocalidad(direccion.getLocalidad());
            manejadorEntidades.merge(direccion);
        }

        manejadorEntidades.getTransaction().commit();
        return direccion;
    }

    @Override
    public void borrar(Direccion entity) {
        manejadorEntidades.getTransaction().begin();

        if(entity != null) {
            manejadorEntidades.remove(entity);
        }

        manejadorEntidades.getTransaction().commit();
    }

    @Override
    public void borrarPorId(Long id) {
        manejadorEntidades.getTransaction().begin();
        Direccion direccion = manejadorEntidades.find(Direccion.class, id);

        if(direccion != null) {
            manejadorEntidades.remove(direccion);
        }

        manejadorEntidades.getTransaction().commit();
    }

    @Override
    public void borrarPorLote(List<Direccion> direcciones) {
        manejadorEntidades.getTransaction().begin();

        for(Direccion indice: direcciones) {
            if(indice != null) {
                manejadorEntidades.remove(indice);
            }
        }
    }

    @Override
    public Direccion encontrarPorId(Long id) {
        return manejadorEntidades.find(Direccion.class, id);
    }

    @Override
    public List<Direccion> encontrarTodos() {
        return manejadorEntidades.createQuery("SELECT d FROM Direccion d", Direccion.class).getResultList();
    }
}