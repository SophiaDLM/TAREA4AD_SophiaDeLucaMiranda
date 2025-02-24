package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class DataConexion {
    private static DataConexion instancia = null;
    private final String ruta = "Tarea4ADSophiaDeLucaMiranda/src/main/resources/servicios.db4o";
    private static ObjectContainer baseDatos;

    private DataConexion() {

    }

    private synchronized static void crearInstancia() {
        if(instancia == null) {
            instancia = new DataConexion();
            instancia.realizarConexion();
        }
    }

    public static ObjectContainer obtenerInstancia() {
        if(instancia == null) {
            crearInstancia();
        }
        return baseDatos;
    }

    public void realizarConexion() {
        baseDatos = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ruta);
    }

    public void cerrarConexion() {
        baseDatos.close();
    }
}