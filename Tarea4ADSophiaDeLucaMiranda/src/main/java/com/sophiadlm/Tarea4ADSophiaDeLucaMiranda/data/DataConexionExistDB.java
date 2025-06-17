package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import javafx.scene.control.Alert;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataConexionExistDB {
    private String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
    private XMLResource recurso;

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

            coleccionParada = DatabaseManager.getCollection(URI, "admin", "admin");

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

            if (coleccionPadre != null && DatabaseManager.getCollection(URI+"/carnets", "admin", "admin") == null) {
                CollectionManagementService manejoServicioColeccion = (CollectionManagementService) coleccionPadre.getService("CollectionManagementService", "1.0");
                manejoServicioColeccion.createCollection("carnets");
                System.out.println("\n>> Colección raíz creada correctamente");
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
                manejoServicioColeccion.createCollection("parada_"+nombreParada);
                System.out.println("\n>> Colección para la parada creada correctamente");
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
     */
    public Document convertirCarnetInicialXML(Long idCarnet, Long idParadaInicial) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.newDocument();

            Element carnet = documento.createElement("carnet");
            documento.appendChild(carnet);

            Element id = documento.createElement("id");
            id.setTextContent(String.valueOf(idCarnet));
            carnet.appendChild(id);

            Element distancia = documento.createElement("distancia");
            distancia.setTextContent("0");
            carnet.appendChild(distancia);

            Element fechaexp = documento.createElement("fechaexp");
            fechaexp.setTextContent(String.valueOf(LocalDate.now()));
            carnet.appendChild(fechaexp);

            Element nvips = documento.createElement("nvips");
            nvips.setTextContent("0");
            carnet.appendChild(nvips);

            Element id_parada_inicial = documento.createElement("id_parada_inicial");
            id_parada_inicial.setTextContent(String.valueOf(idParadaInicial));
            carnet.appendChild(id_parada_inicial);

            return documento;

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            e.printStackTrace();
            error.showAndWait();

            return null;
        }
    }

    /***
     *
     */
    public void introducirCarnetEnColeccion(Document carnetInicial, String nombreParada, String nombrePeregrino) {
        try {
            crearSubColeccionParada(nombreParada);

            Collection coleccionDestino = conectarExistDB(URI+"/carnets/"+"parada_"+nombreParada);

            if(coleccionDestino != null) {
                XMLResource recurso = (XMLResource) coleccionDestino.createResource("carnet_"+nombrePeregrino, "XMLResource");

                TransformerFactory fabricaTransformador = TransformerFactory.newInstance();
                Transformer transformador = fabricaTransformador.newTransformer();
                DOMSource fuente = new DOMSource(carnetInicial);

                //Se usa StringWriter para que existDB pueda procesarlo de mejor manera. Es simple y legible
                StringWriter escritor = new StringWriter();
                StreamResult resultadoStream = new StreamResult(escritor);
                transformador.transform(fuente, resultadoStream);

                recurso.setContent(escritor.toString());
                coleccionDestino.storeResource(recurso);

                System.out.println("\n>> Carnet guardado en la colección correctamente");

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Fatal Error");
                error.setHeaderText("Ocurrió un error al almacenar el archivo XML en existDB");
                error.showAndWait();
            }

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            e.printStackTrace();
            error.showAndWait();
        }
    }

    /***
     *
     */
    public List<String> obtenerCarnetsPorParada(String nombreParada) {
        List<String> nombreCarnets = new ArrayList<>();

        try {
            Collection coleccionParada = conectarExistDB(URI + "/carnets" + "/parada_"+nombreParada);

            if(coleccionParada != null) {
                String[] listaRecursos = coleccionParada.listResources();

                for(String indice: listaRecursos) {
                    nombreCarnets.add(indice);
                }

            }
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            e.printStackTrace();
            error.showAndWait();
        }

        return nombreCarnets;
    }
}