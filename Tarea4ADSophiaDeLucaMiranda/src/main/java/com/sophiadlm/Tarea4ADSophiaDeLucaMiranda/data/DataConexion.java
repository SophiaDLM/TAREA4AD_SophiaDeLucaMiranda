package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class DataConexion {
    private static DataConexion instancia = null;
    private File ficheroDb4oProp = new File("Tarea4ADSophiaDeLucaMiranda/src/main/resources/db4o.properties");
    private static ObjectContainer baseDatos;

    private DataConexion() {

    }

    private synchronized static void crearInstancia() {
        if(instancia == null) {
            instancia = new DataConexion();
            instancia.realizarConexion();
        }
    }

    public static ObjectContainer obtenerInstanciaObjectContainer() {
        if(instancia == null) {
            crearInstancia();
        }
        return baseDatos;
    }

    public static DataConexion obtenerInstanciaDataConexion() {
        if(instancia == null) {
            crearInstancia();
        }
        return instancia;
    }

    public void realizarConexion() {
        Properties propiedades = new Properties();
        FileInputStream fis;

        try {
            fis = new FileInputStream(ficheroDb4oProp);
            propiedades.load(fis);

            String ruta = propiedades.getProperty("Ruta");

            baseDatos = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ruta);
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción desconocida");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    public void cerrarConexion() {
        if (baseDatos != null && !baseDatos.ext().isClosed()) {
            baseDatos.close();
        }
    }
}