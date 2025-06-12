package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DataConexionObjectDB {
    private static final EntityManagerFactory fabricaManejadorEntidades = Persistence.createEntityManagerFactory("OBDB_envios.odb");

    public static EntityManager obtenerInstancia() {
        return fabricaManejadorEntidades.createEntityManager();
    }

    public void cerrarConexion() {
        if(fabricaManejadorEntidades != null && fabricaManejadorEntidades.isOpen()) {
            fabricaManejadorEntidades.close();
        }
    }
}