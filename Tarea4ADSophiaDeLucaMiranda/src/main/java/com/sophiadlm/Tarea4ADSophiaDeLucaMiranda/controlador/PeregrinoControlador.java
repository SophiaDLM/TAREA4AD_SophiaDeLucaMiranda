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
 * Clase PeregrinoControlador que se encarga de gestionar las acciones
 * que puede realizar un usuario del tipo peregrino como acceder a la ayuda
 * de usuario (no implementada aún), cerrar sesión si así lo desea, editar datos
 * como su nombre o nacionalidad (y en un futuro su email), e incluso exportar
 * su carnet en un documento XML.
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
    private SesionUsuario su;

    @Autowired
    private PeregrinoServicio pes;

    @Autowired
    private CarnetServicio cas;

    @Autowired
    private EstanciaServicio ess;

    @Autowired
    private PeregrinoParadaServicio pps;


    /***
     * Método cerrarSesion que lanza una alerta pidiendo al usuario que
     * confirme su decisión. Si el usuario pulsa en el botón de aceptar, se
     * camibia la ventana a la de INICIARSESION y se asignan las credenciales
     * a null para indicar que el usuario ha vuelto a ser un invitado.
     *
     * En este método sí se maneja la sesión del usuario, puesto que
     * pueden existir varios peregrinos en la base de datos y se pide que
     * se recojan los datos de este para otros métodos.
     */
    @FXML
    public void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación Cerrar Sesión");
        confirmacion.setContentText("¿Está seguro que desea cerrar sesión?");

        ButtonType confirmar = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

        if (confirmar == ButtonType.OK) {
            me.cambiarEscena(VistaFxml.INICIARSESION);
            su.setCredenciales(null);
        }
    }

    /***
     * Método mostrarAyuda que, como está incompleto, sólo se encarga
     * de mostrar una alerta informativa.
     */
    @FXML
    public void mostrarAyuda() {
        Alert sinImplementar = new Alert(Alert.AlertType.INFORMATION);
        sinImplementar.setTitle("Ayuda No Implementada");
        sinImplementar.setHeaderText("¡Oops!");
        sinImplementar.setContentText("La ayuda para el usuario aún no está disponible");
        sinImplementar.showAndWait();
    }

    /***
     * Método editarPeregrino que, como no está implementado, sólo se encarga
     * de mostrar una alerta informativa.
     */
    @FXML
    public void editarPeregrino() {
        Alert sinImplementar = new Alert(Alert.AlertType.INFORMATION);
        sinImplementar.setTitle("Edición No Implementada");
        sinImplementar.setHeaderText("¡Oops!");
        sinImplementar.setContentText("La edición aún no está disponible");
        sinImplementar.showAndWait();
    }

    /***
     * Método exportarCarnetXML que recoge los datos de la base de datos y construye,
     * utilizando la tecnología DOM, un documento XML que se encontrará disponible en
     * la carpeta denominada como exportable.
     */
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
            File fichero = new File("C:\\Users\\Sophi\\Downloads\\TAREA3AD_SophiaDeLucaMiranda-master\\TAREA3AD_SophiaDeLucaMiranda-master\\Tarea3ADSophiaDeLucaMiranda\\src\\main\\exportable\\" + peregrinoActual.getNombre().toLowerCase() + ".xml");
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
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción del tipo ParserConfigurationException");
            error.setContentText(pce.getMessage());
            error.showAndWait();

        } catch (TransformerConfigurationException tce) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción del tipo TransformerConfigurationException");
            error.setContentText(tce.getMessage());
            error.showAndWait();

        } catch (TransformerException te) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción del tipo TransformerException");
            error.setContentText(te.getMessage());
            error.showAndWait();

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción desconocida");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    /***
     * Método initialize que sirve para cargar valores al arrancar la aplicación.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Peregrino peregrino = obtenerPeregrino();

        tfNombre.setText(peregrino.getNombre());
        tfNacionalidad.setText(peregrino.getNacionalidad());
    }

    /***
     * Método obtenerPeregrino que sirve para obtener el objeto peregrino de la base
     * de datos accediendo a él gracias a la sesión guardada al iniciar sesión.
     *
     * @return peregrinoActual si se encontró el peregrino, sino, debería lanzar un error.
     */
    private Peregrino obtenerPeregrino() {
        Credenciales credencialesActuales = su.getCredenciales();
        Peregrino peregrinoActual = null;

        if (credencialesActuales != null) {
            peregrinoActual = pes.encontrarPorId(credencialesActuales.getId());
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Fatal error");
            error.setContentText("No se encontro el peregrino");
        }
        return peregrinoActual;
    }

    /***
     * Método obtenerCarnet que sirve para obtener el objeto carnet del peregrino.
     *
     * @param idPeregrino que se puede obtener desde el método obtenerPeregrino.
     * @return carnetActual.
     */
    private Carnet obtenerCarnet(Long idPeregrino) {
        Carnet carnetActual = cas.encontrarPorId(idPeregrino);
        return carnetActual;
    }

    /***
     * Método obtenerParadas que sirve para obtener una lista de todas las paradas asociadas
     * a un peregrino.
     *
     * @param idPeregrino que se puede obtener desde el método obtenerPeregrino.
     * @return la lista de paradasActuales.
     */
    private List<Parada> obtenerParadas(Long idPeregrino) {
        List<Parada> paradasActuales = pps.obtenerParadaPeregrino(idPeregrino);
        return paradasActuales;
    }

    /***
     * Método obtenerEstancias que sirve para obtener una lista de todas las estancias asociadas
     * a un peregrino.
     *
     * @param idPeregrino que se puede obtener desde el método obtenerPeregrino.
     * @return la lista de estanciasActuales.
     */
    private List<Estancia> obtenerEstancias(Long idPeregrino) {
        List<Estancia> estanciasActuales = ess.encontrarPorIdPeregrino(idPeregrino);
        return estanciasActuales;
    }
}