package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.XMLResource;

//TERMINAR LUEGO - PREGUNTAR CHATGPT Y/O VER APUNTES AULA
public class DataConexionExistDB {
    private String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/carnets";
    private Collection coleccion = null;
    private XMLResource recurso = null;

    public void conexionExistDB(String url) {
        coleccion = conectarExistDB(url);
    }

    public Collection conectarExistDB(String URI) {
        try {
            Class clase = Class.forName("org.exist.xmldb.DatabaseImpl");
            Database baseDatos = (Database) clase.newInstance();
            baseDatos.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(baseDatos);

            coleccion = DatabaseManager.getCollection(URI, "admin", "");

            //VER LUEGO DE QUÉ SE PUEDE HACER

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
