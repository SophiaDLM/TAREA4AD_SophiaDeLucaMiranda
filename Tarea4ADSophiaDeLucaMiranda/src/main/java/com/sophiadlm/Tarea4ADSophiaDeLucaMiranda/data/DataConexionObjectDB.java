package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DataConexionObjectDB {
    private static final EntityManagerFactory fabricaManejadorEntidades = Persistence.createEntityManagerFactory("objectdb:OBDB_envios.odb");

    public static EntityManager obtenerInstancia() {
        return fabricaManejadorEntidades.createEntityManager();
    }

    public void cerrarConexion() {
        if(fabricaManejadorEntidades != null && fabricaManejadorEntidades.isOpen()) {
            fabricaManejadorEntidades.close();
        }
    }
}