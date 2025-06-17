package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Carnet;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Estancia;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Parada;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Peregrino;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.CarnetServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.EstanciaServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.PeregrinoParadaServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.PeregrinoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DataConexionMongoDB {
    @Autowired
    private PeregrinoServicio peregrinoServicio;

    @Autowired
    private CarnetServicio carnetServicio;

    @Autowired
    private PeregrinoParadaServicio peregrinoParadaServicio;

    @Autowired
    private EstanciaServicio estanciaServicio;

    @Autowired
    private MongoTemplate plantillaMongo;

    /***
     *
     */
    public String exportarCarnetXML(Peregrino peregrinoActual) {
        Carnet carnetPeregrino = obtenerCarnet(peregrinoActual.getId());
        peregrinoActual.setCarnet(carnetPeregrino);

        List<Parada> listaParadasPeregrino = obtenerParadas(peregrinoActual.getId());
        peregrinoActual.setListaParadas(listaParadasPeregrino);

        List<Estancia> listaEstanciasPeregrino = obtenerEstancias(peregrinoActual.getId());
        peregrinoActual.setListaEstancias(listaEstanciasPeregrino);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            DOMImplementation implementacion = db.getDOMImplementation();

            Document documento = implementacion.createDocument(null, "carnet", null);
            Element carnet = documento.getDocumentElement();

            documento.setXmlVersion("1.0");
            ProcessingInstruction pi = documento.createProcessingInstruction("xml-stylesheet", "type=\"text/xml\" href=\"test.xsl\"");
            documento.insertBefore(pi, carnet);

            Element idCarnet = documento.createElement("id");
            idCarnet.setTextContent(String.valueOf(carnetPeregrino.getId()));
            carnet.appendChild(idCarnet);

            Element fechaExpedicion = documento.createElement("fechaexp");
            fechaExpedicion.setTextContent(String.valueOf(carnetPeregrino.getFechaexp()));
            carnet.appendChild(fechaExpedicion);

            Element expedidoEn = documento.createElement("expedidoen");
            expedidoEn.setTextContent(carnetPeregrino.getParadaInicial().getNombre());
            carnet.appendChild(expedidoEn);

            Element peregrino = documento.createElement("peregrino");

            Element nombre = documento.createElement("nombre");
            nombre.setTextContent(peregrinoActual.getNombre());
            peregrino.appendChild(nombre);

            Element nacionalidad = documento.createElement("nacionalidad");
            nacionalidad.setTextContent(peregrinoActual.getNacionalidad());
            peregrino.appendChild(nacionalidad);

            carnet.appendChild(peregrino);

            Element hoy = documento.createElement("hoy");
            hoy.setTextContent(LocalDate.now().toString());
            carnet.appendChild(hoy);

            Element distanciaTotal = documento.createElement("distanciatotal");
            distanciaTotal.setTextContent(String.valueOf(carnetPeregrino.getDistancia()));
            carnet.appendChild(distanciaTotal);

            Element paradas = documento.createElement("paradas");
            for (int i = 0; i < listaParadasPeregrino.size(); i++) {
                Parada paradaActual = listaParadasPeregrino.get(i);

                Element parada = documento.createElement("parada");

                Element orden = documento.createElement("orden");
                orden.setTextContent(String.valueOf(i + 1));
                parada.appendChild(orden);

                Element nombreParada = documento.createElement("nombre");
                nombreParada.setTextContent(paradaActual.getNombre());
                parada.appendChild(nombreParada);

                Element regionParada = documento.createElement("region");
                regionParada.setTextContent(String.valueOf(paradaActual.getRegion()));
                parada.appendChild(regionParada);

                paradas.appendChild(parada);
            }
            carnet.appendChild(paradas);

            Element estancias = documento.createElement("estancias");
            for (int i = 0; i < listaEstanciasPeregrino.size(); i++) {
                Estancia estanciaActual = listaEstanciasPeregrino.get(i);

                Element estancia = documento.createElement("estancia");

                Element idEstancia = documento.createElement("id");
                idEstancia.setTextContent(String.valueOf(estanciaActual.getId()));
                estancia.appendChild(idEstancia);

                Element fecha = documento.createElement("fecha");
                fecha.setTextContent(String.valueOf(estanciaActual.getFecha()));
                estancia.appendChild(fecha);

                Element paradaNombre = documento.createElement("parada");
                paradaNombre.setTextContent(estanciaActual.getParada().getNombre());
                estancia.appendChild(paradaNombre);

                Element vip = documento.createElement("vip");
                if (estanciaActual.isVip()) {
                    estancia.appendChild(vip);
                }
                estancias.appendChild(estancia);
            }
            carnet.appendChild(estancias);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter escritor = new StringWriter();
            t.transform(new DOMSource(documento), new StreamResult(escritor));

            return escritor.toString();

        } catch (Exception e) {
            System.out.println(">> ERROR AL INTENTAR CREAR EL CARNET");
            e.printStackTrace();

            return null;
        }
    }

    /***
     *
     */
    public void crearBackupCarnets() {
        List<Peregrino> listaPeregrinos = peregrinoServicio.encontrarTodos();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.newDocument();

            Element raiz = documento.createElement("backupCarnets");
            documento.appendChild(raiz);

            for (Peregrino indice: listaPeregrinos) {
                String carnetXML = exportarCarnetXML(indice);
                if (carnetXML != null) {
                    Document documentoCarnet = db.parse(new InputSource(new StringReader(carnetXML)));
                    Node nodoCarnet = documento.importNode(documentoCarnet.getDocumentElement(), true);
                    raiz.appendChild(nodoCarnet);
                }
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter escritor = new StringWriter();
            t.transform(new DOMSource(documento), new StreamResult(escritor));
            String xmlFinal = escritor.toString();

            org.bson.Document documentoMongo = new org.bson.Document();
            documentoMongo.put("nombre", "backupcarnets_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")));
            documentoMongo.put("contenido", xmlFinal);

            plantillaMongo.insert(documentoMongo, "backupCarnets");

            System.out.println("\n>> Backup creado con éxito");

        } catch (Exception e) {
            System.out.println(">> ERROR AL INTENTAR CREAR EL BACKUP");
            e.printStackTrace();
        }
    }

    private Carnet obtenerCarnet(Long idPeregrino) {
        return carnetServicio.encontrarPorId(idPeregrino);
    }

    private List<Parada> obtenerParadas(Long idPeregrino) {
        return peregrinoParadaServicio.obtenerParadaPeregrino(idPeregrino);
    }

    private List<Estancia> obtenerEstancias(Long idPeregrino) {
        return estanciaServicio.encontrarPorIdPeregrino(idPeregrino);
    }
}