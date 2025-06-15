package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.controlador;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config.ManejadorEscenas;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.*;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.*;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista.VistaFxml;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Clase Peregrino Controlador que se encarga de gestionar las acciones que puede realizar un usuario del tipo peregrino
 * como exportar su carnet en un documento XML y cerrar la sesión si así lo desea.
 */
@Controller
public class PeregrinoControlador implements Initializable {
    //Elementos relacionados con el archivo FXML:
    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfNacionalidad;


    //Elementos relacionados con el manejo de las escenas:
    @Lazy
    @Autowired
    private ManejadorEscenas me;


    //Elementos relacionados con la manipulación de la base de datos:
    @Autowired
    private SesionUsuario sesionUsuario;

    @Autowired
    private PeregrinoServicio peregrinoServicio;

    @Autowired
    private CarnetServicio carnetServicio;

    @Autowired
    private EstanciaServicio estanciaServicio;

    @Autowired
    private PeregrinoParadaServicio peregrinoParadaServicio;


    /***
     * Método para Cerrar Sesión que lanza una alerta pidiendo al usuario que confirme su decisión. Si el usuario pulsa en el
     * botón de aceptar, se cambia la ventana a la de iniciar sesión y se asignan las credenciales a null para indicar
     * que el usuario ha vuelto a ser de tipo invitado. En este método sí se maneja la sesión del usuario, puesto que
     * pueden existir varios peregrinos en la base de datos y se pide que se recojan los datos de este para otros métodos.
     */
    @FXML
    public void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación Cerrar Sesión");
        confirmacion.setContentText("¿Está seguro que desea cerrar sesión?");

        ButtonType confirmar = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

        if (confirmar == ButtonType.OK) {
            me.cambiarEscena(VistaFxml.INICIARSESION);
            sesionUsuario.setCredenciales(null);
        }
    }

    /***
     * Método para Exportar un Carnet en XML que recoge los datos de la base de datos y construye, utilizando la
     * tecnología DOM, un documento XML que se encontrará disponible en la carpeta denominada como "exportable".
     */
    //EN MONGO DB BACKUP NOMBRE DEBE TENER FECHA Y HORA!!
    //Modificar para que muestre todas las paradas por las que se ha pasado, no importa si salen repetidas
    @FXML
    public void exportarCarnetXML() {
        Peregrino peregrinoActual = obtenerPeregrino();

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

            DOMSource fuente = new DOMSource(documento);
            File fichero = new File("Tarea4ADSophiaDeLucaMiranda/src/main/exportable/" + peregrinoActual.getNombre().trim().toLowerCase() + ".xml"); //NO HACE EL TRIM. MIRAR LUEGO
            StreamResult sr = new StreamResult(fichero);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(fuente, sr);

            Alert carnetExportado = new Alert(Alert.AlertType.INFORMATION);
            carnetExportado.setTitle("Carnet Exportado");
            carnetExportado.setHeaderText("El carnet se encuentra en la carpeta denomida \"exportable\"");
            carnetExportado.showAndWait();

        } catch (ParserConfigurationException pce) {
            alertaError("Ocurrió una excepción del tipo ParserConfigurationException", pce.getMessage());

        } catch (TransformerConfigurationException tce) {
            alertaError("Ocurrió una excepción del tipo TransformerConfigurationException", tce.getMessage());

        } catch (TransformerException te) {
            alertaError("Ocurrió una excepción del tipo TransformerException", te.getMessage());

        } catch (Exception excepcion) {
            alertaError("Ocurrió una excepción desconocida", excepcion.getMessage());
        }
    }

    /***
     * Método Initialize que sirve para cargar valores al arrancar la aplicación.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Peregrino peregrino = obtenerPeregrino();

        tfNombre.setText(peregrino.getNombre());
        tfNacionalidad.setText(peregrino.getNacionalidad());
    }

    /***
     * Método para Obtener un Peregrino que sirve para obtener el objeto peregrino de la base de datos, accediendo a él
     * gracias a la sesión guardada al iniciar sesión.
     *
     * @return peregrino actual si se encontró el peregrino.
     */
    private Peregrino obtenerPeregrino() {
        Credenciales credencialesActuales = sesionUsuario.getCredenciales();
        Peregrino peregrinoActual = null;

        if (credencialesActuales != null) {
            peregrinoActual = peregrinoServicio.encontrarPorId(credencialesActuales.getId());
        } else {
            alertaError("", "No se encontró al peregrino");
        }
        return peregrinoActual;
    }

    /***
     * Método para Obtener un Carnet que sirve para obtener el objeto carnet del peregrino.
     *
     * @param idPeregrino que se puede obtener desde el método obtenerPeregrino, es un dato de tipo Long.
     * @return el carnet actual del peregrino.
     */
    private Carnet obtenerCarnet(Long idPeregrino) {
        return carnetServicio.encontrarPorId(idPeregrino);
    }

    /***
     * Método para Obtener Paradas que sirve para obtener una lista de todas las paradas asociadas a un peregrino.
     *
     * @param idPeregrino que se puede obtener desde el método obtenerPeregrino, es un dato de tipo Long.
     * @return la lista de paradas actuales.
     */
    private List<Parada> obtenerParadas(Long idPeregrino) {
        return peregrinoParadaServicio.obtenerParadaPeregrino(idPeregrino);
    }

    /***
     * Método para Obtener Estancias que sirve para obtener una lista de todas las estancias asociadas a un peregrino.
     *
     * @param idPeregrino que se puede obtener desde el método obtenerPeregrino, es un dato de tipo Long.
     * @return la lista de estancias actuales.
     */
    private List<Estancia> obtenerEstancias(Long idPeregrino) {
        return estanciaServicio.encontrarPorIdPeregrino(idPeregrino);
    }

    /***
     * Método para mostrar Alerta de Error que se encarga de lanzar una alerta avisando qué es lo que ha fallado en el
     * programa.
     *
     * @param textoError al cual se le pasa un String del texto personalizado.
     * @param excepcionTexto al que se le pasa un String del mensaje de la excepción para obtener información más
     *                       concreta del error.
     */
    private void alertaError(String textoError, String excepcionTexto) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Fatal Error");
        error.setHeaderText(textoError);
        error.setContentText(excepcionTexto);
        error.showAndWait();
    }
}