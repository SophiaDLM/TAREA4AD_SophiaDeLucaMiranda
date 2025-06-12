package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexionObjectDB;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.EnvioACasa;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

public class EnvioACasaRepositorio {

    public boolean guardarObjectDB(EnvioACasa entity) {
        EntityManager manejadorEntidad = DataConexionObjectDB.obtenerInstancia();

        try {
            manejadorEntidad.getTransaction().begin();
            manejadorEntidad.persist(entity);
            manejadorEntidad.getTransaction().commit();
            return true;

        } catch (Exception e) {
            manejadorEntidad.getTransaction().rollback();
            System.out.println(">> FATAL ERROR - No se ha podido guardar el envío: ");
            e.printStackTrace();

        } finally {
            manejadorEntidad.close();
        }
        return false;
    }

    public List<EnvioACasa> encontrarTodosObjectDB() {
        List<EnvioACasa> listaEnvios = null;
        EntityManager manejadorEntidad = DataConexionObjectDB.obtenerInstancia();

        try {
            TypedQuery<EnvioACasa> consulta = manejadorEntidad.createQuery("SELECT e FROM EnvioACasa e", EnvioACasa.class);
            listaEnvios = consulta.getResultList();
            return listaEnvios;

        } catch (Exception e) {
            System.out.println(">> FATAL ERROR - No se han podido encontrar los envíos: ");
            e.printStackTrace();

        } finally {
            manejadorEntidad.close();
        }
        return listaEnvios;
    }

    public List<EnvioACasa> encontrarEnviosPorParada(Long idParada) {
        List<EnvioACasa> listaEnviosParada = null;
        EntityManager manejadorEntidad = DataConexionObjectDB.obtenerInstancia();

        try {
            TypedQuery<EnvioACasa> consulta = manejadorEntidad.createQuery("SELECT e FROM EnvioACasa e WHERE e.idParada = :idParada", EnvioACasa.class);
            consulta.setParameter("idParada", idParada);
            listaEnviosParada = consulta.getResultList();

        } catch (Exception e) {
            System.out.println(">> FATAL ERROR - No se han podido encontrar los envíos: ");
            e.printStackTrace();

        } finally {
            manejadorEntidad.close();
        }
        return listaEnviosParada;
    }
}