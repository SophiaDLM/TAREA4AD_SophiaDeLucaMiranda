package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexionObjectDB;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public class DireccionServicio {
//
//    private EntityManager manejadorEntidades = DataConexionObjectDB.obtenerInstancia();
//
//    @Autowired
//    private DireccionRepositorio dr;
//
//    public Direccion guardar(Direccion entity) {
//        manejadorEntidades.getTransaction().begin();
//        manejadorEntidades.persist(entity);
//        manejadorEntidades.getTransaction().commit();
//        return entity;
//    }
//
//    public Direccion actualizar(Direccion entity) {
//        manejadorEntidades.getTransaction().begin();
//        Direccion direccion = manejadorEntidades.find(Direccion.class, entity);
//
//        if(direccion != null) {
//            direccion.setDireccion(direccion.getDireccion());
//            direccion.setLocalidad(direccion.getLocalidad());
//            manejadorEntidades.merge(direccion);
//        }
//
//        manejadorEntidades.getTransaction().commit();
//        return direccion;
//    }
//
//    public void borrar(Direccion entity) {
//        manejadorEntidades.getTransaction().begin();
//
//        if(entity != null) {
//            manejadorEntidades.remove(entity);
//        }
//
//        manejadorEntidades.getTransaction().commit();
//    }
//
//    public void borrarPorId(Long id) {
//        manejadorEntidades.getTransaction().begin();
//        Direccion direccion = manejadorEntidades.find(Direccion.class, id);
//
//        if(direccion != null) {
//            manejadorEntidades.remove(direccion);
//        }
//
//        manejadorEntidades.getTransaction().commit();
//    }
//
//    public void borrarPorLote(List<Direccion> direcciones) {
//        manejadorEntidades.getTransaction().begin();
//
//        for(Direccion indice: direcciones) {
//            if(indice != null) {
//                manejadorEntidades.remove(indice);
//            }
//        }
//    }
//
//    public Direccion encontrarPorId(Long id) {
//        return manejadorEntidades.find(Direccion.class, id);
//    }
//
//    public List<Direccion> encontrarTodos() {
//        return manejadorEntidades.createQuery("SELECT d FROM Direccion d", Direccion.class).getResultList();
//    }
//}