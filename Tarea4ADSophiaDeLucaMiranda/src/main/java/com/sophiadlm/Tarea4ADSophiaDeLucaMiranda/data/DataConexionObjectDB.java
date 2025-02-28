package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

//NO SE MUY BIEN QUE PASA CON LA LIBRERIA DE OBJECTDB
public class DataConexionObjectDB {
    private static DataConexionObjectDB instancia = null;
    private final String ruta = "envios.odb";
    private static EntityManagerFactory fabricaManejadorEntidades;
    private static EntityManager manejadorEntidades;

    private DataConexionObjectDB() {

    }

    private synchronized static void crearInstancia() {
        if(instancia == null) {
            instancia = new DataConexionObjectDB();
            instancia.realizarConexion();
        }
    }

    public static EntityManager obtenerInstancia() {
        if(instancia == null) {
            crearInstancia();
        }
        return manejadorEntidades;
    }

    public void realizarConexion() {
        fabricaManejadorEntidades = Persistence.createEntityManagerFactory(ruta);
        manejadorEntidades = fabricaManejadorEntidades.createEntityManager();
    }

    public void cerrarConexion() {
        if(manejadorEntidades != null) {
            manejadorEntidades.close();
        }

        if(fabricaManejadorEntidades != null) {
            fabricaManejadorEntidades.close();
        }
    }
}