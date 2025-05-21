package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexionObjectDB;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.EnvioACasa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class EnvioACasaRepositorio {
    public boolean guardarObjectDB(EnvioACasa entity) {
        //MODIFICAR
        EntityManager manejadorEntidad = DataConexionObjectDB.obtenerInstancia();

        try {
            manejadorEntidad.getTransaction().begin();
            manejadorEntidad.persist(entity);
            manejadorEntidad.getTransaction().commit();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manejadorEntidad.close();
        }
    }

    public List<EnvioACasa> encontrarTodosObjectDB(EnvioACasa entity) {
        EntityManager manejadorEntidad = DataConexionObjectDB.obtenerInstancia();

        try {
            TypedQuery<EnvioACasa> consulta = manejadorEntidad.createQuery("SELECT e FROM EnvioACasa e", EnvioACasa.class);
            List<EnvioACasa> listaEnvios = consulta.getResultList();
            return listaEnvios;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manejadorEntidad.close();
        }
    }



//    public List<EnvioACasa> obtenerEnviosPorParada(Long idParada)
//    {
//        EntityManager em = ObjectDBConnection.getEntityManager();
//
//        TypedQuery<EnvioACasa> query = em.createQuery("SELECT e FROM EnvioACasa e WHERE e.idParada = :idParada", EnvioACasa.class);
//
//        query.setParameter("idParada", idParada);
//
//        List<EnvioACasa> envios = query.getResultList();
//
//        em.close();
//
//        return envios;
//    }
}