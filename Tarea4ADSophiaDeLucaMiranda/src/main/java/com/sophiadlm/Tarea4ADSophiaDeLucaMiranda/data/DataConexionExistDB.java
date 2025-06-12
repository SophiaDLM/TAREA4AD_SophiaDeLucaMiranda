package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import javafx.scene.control.Alert;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

public class DataConexionExistDB {
    private String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
    private XMLResource recurso;

    /***
     *
     * @param url
     */
    public void conexionExistDB(String url) {
        Collection coleccion = conectarExistDB(url);
    }

    /***
     *
     * @param URI
     * @return
     */
    public Collection conectarExistDB(String URI) {
        Collection coleccionParada = null;

        try {
            Class<?> clase = Class.forName("org.exist.xmldb.DatabaseImpl");
            Database baseDatos = (Database) clase.getDeclaredConstructor().newInstance();
            baseDatos.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(baseDatos);

            coleccionParada = DatabaseManager.getCollection(URI, "admin", "");

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }

        return coleccionParada;
    }

    /***
     *
     */
    public void crearColeccionRaiz() {
        try {
            Collection coleccionPadre = conectarExistDB(URI);

            if (coleccionPadre != null && DatabaseManager.getCollection(URI+"/carnets", "admin", "") == null) {
                CollectionManagementService manejoServicioColeccion = (CollectionManagementService) coleccionPadre.getService("CollectionManagementService", "1.0");
                manejoServicioColeccion.createCollection("carnets");
                System.out.println("\n>> Colección raíz creada correctamente\n");
            }

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }


    /***
     *
     * @param nombreParada
     */
    public void crearSubColeccionParada(String nombreParada) {
        try {
            crearColeccionRaiz();
            Collection coleccionCarnets = conectarExistDB(URI+"/carnets");

            if(coleccionCarnets != null) {
                CollectionManagementService manejoServicioColeccion = (CollectionManagementService) coleccionCarnets.getService("CollectionManagementService", "1.0");
                manejoServicioColeccion.createCollection(nombreParada);
                System.out.println("\n>> Colección para la parada creada correctamente\n");
            }


        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }


}