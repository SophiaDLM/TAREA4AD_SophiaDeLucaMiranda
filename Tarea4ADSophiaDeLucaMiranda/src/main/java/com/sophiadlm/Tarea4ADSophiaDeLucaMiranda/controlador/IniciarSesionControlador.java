package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.controlador;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config.ManejadorEscenas;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.*;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.*;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista.VistaFxml;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Clase IniciarSesionControlador que se encarga de manejar las acciones
 * disponibles tanto de un usuario invitado como de un usuario existente en la base de datos.
 * Estás acciones son acceder a la ayuda de usuario (no implementada aún), iniciar sesión con sus credenciales,
 * registrarse como peregrino y cerrar sesión si así lo desea.
 * Esta clase implementa Initializable para el uso de JavaFX.
 */
@Controller
public class IniciarSesionControlador implements Initializable {
    //Elementos relacionados con el archivo FXML:
    @FXML
    private GridPane panelPrincipal;

    @FXML
    private TextField tfUsuario;

    @FXML
    private PasswordField pfContraseña;

    @FXML
    private GridPane panelRegistrarse;

    @FXML
    private TextField tfUsuarioP;

    @FXML
    private PasswordField pfContraseñaP;

    @FXML
    private TextField tfNombre;

    @FXML
    private ChoiceBox<String> cbNacionalidad;

    @FXML
    private ChoiceBox<String> cbParadaInicial;

    //Elementos relacionados con el manejo de las escenas:
    @Lazy
    @Autowired
    private ManejadorEscenas me;

    //Elementos relacionados con la manipulación de la base de datos:
    @Autowired
    private CredencialesServicio cs;

    @Autowired
    private SesionUsuario su;

    @Autowired
    private PeregrinoServicio pes;

    @Autowired
    private CarnetServicio cas;

    @Autowired
    private ParadaServicio pas;

    @Autowired
    private PeregrinoParadaServicio pps;

    /***
     * Método mostrarAyuda que, como está incompleto, sólo se encarga
     * de mostrar una alerta informativa.
     */
    @FXML
    public void mostrarAyuda() {
        //AÑADIR SISTEMA DE AYUDA
        Alert sinImplementar = new Alert(Alert.AlertType.INFORMATION);
        sinImplementar.setTitle("Ayuda No Implementada");
        sinImplementar.setHeaderText("¡Oops!");
        sinImplementar.setContentText("La ayuda para el usuario aún no está disponible");
        sinImplementar.showAndWait();
    }

    /***
     * Método volver que se utiliza para cambiar el panel de registrar peregrino al de iniciar sesión y borra los
     * datos almacenados en el formulario después de confirmar que quiere volver.
     */
    @FXML
    public void volver() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación Para Volver");
        confirmacion.setContentText("¿Está seguro que desea volver a la pantalla principal?");

        ButtonType confirmar = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

        if (confirmar == ButtonType.OK) {
            cambiarPanelIniciarSesion();
            tfUsuarioP.clear();
            pfContraseñaP.clear();
            tfNombre.clear();
            cbNacionalidad.getSelectionModel().clearSelection();
            cbParadaInicial.getSelectionModel().clearSelection();
        }
    }

    /***
     * Método cambiarPanelIniciarSesion que cambia la visibilidad de los paneles.
     */
    @FXML
    public void cambiarPanelIniciarSesion() {
        panelPrincipal.setVisible(true);
        panelRegistrarse.setVisible(false);
    }

    /***
     * Método cambiarPanelRegistrarse que cambia la visibilidad de los paneles.
     */
    @FXML
    public void cambiarPanelRegistrarse() {
        panelPrincipal.setVisible(false);
        panelRegistrarse.setVisible(true);
    }

    /***
     * Método iniciarSesion que obtiene las credenciales de la base de datos, las almacena en un objeto del
     * tipo SesionUsuario para utilizarlas posteriormente y en función del tipo de usuario que esté accediendo,
     * se le muestra la vista correspondiente.
     *
     * Si no se puede encontrar el usuario, se lanza una alerta infomando del error.
     */
    @FXML
    public void iniciarSesion() {
        Credenciales credenciales = cs.encontrarPorNombreUsuario(tfUsuario.getText());
        su.setCredenciales(credenciales);

        if(cs.autenticar(tfUsuario.getText(), pfContraseña.getText()).equals(TipoUsuario.ADMINISTRADOR)) {
            me.cambiarEscena(VistaFxml.ADMINISTRADOR);
        } else if(cs.autenticar(tfUsuario.getText(), pfContraseña.getText()).equals(TipoUsuario.PARADA)) {
            me.cambiarEscena(VistaFxml.PARADA);
        } else if(cs.autenticar(tfUsuario.getText(), pfContraseña.getText()).equals(TipoUsuario.PEREGRINO)) {
            me.cambiarEscena(VistaFxml.PEREGRINO);
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("No se ha encontrado al usuario");
            error.setContentText("Regístrese o asegúrese que ha introducido los datos correctamente");
            error.showAndWait();
        }
    }

    /***
     * Método nuevoPeregrino que añade un nuevo peregrino, obteniendo los datos de los campos en el formulario
     * y validándolos. Si todos los datos introducidos son válidos, se crea el objeto credenciales, el peregrino con
     * su carnet con la referencia a la parada inicial y se registra en la tabla peregrino_parada que pasó por allí.
     *
     * Si todo el proceso sale bien, se lanza una alerta, sino, se avisa de que ha ocurrido una excepción o los
     * datos son inválidos.
     *
     * OJO - Es posible que se guarden algunos datos en la base de datos, pero no todos, puesto que faltaría
     * agregar una variable boolean que controlara que si todo es válido, se realicen las operaciones de inserción.
     * Esto queda como mejora para la siguiente tarea.
     */
    @FXML
    public void nuevoPeregrino() {
        try {
            String usuario = tfUsuarioP.getText();
            String contraseña = pfContraseñaP.getText();
            String nombre = tfNombre.getText();
            String nacionalidad = cbNacionalidad.getSelectionModel().getSelectedItem();
            String paradaSeleccionada = cbParadaInicial.getSelectionModel().getSelectedItem();

            //AÑADIR COMPROBACIÓN DE SI EXISTE EL PEREGRINO
            if (usuario.matches("[a-zA-Z0-9_]+")) {
                if (contraseña.matches("[a-zA-Z0-9_]{8}")) {
                    if (validarNombre(nombre)) {
                        if (nacionalidad != null) {
                            if (paradaSeleccionada != null) {
                                String[] campos = paradaSeleccionada.split(" - ");
                                Long idParada = Long.parseLong(campos[0]);

                                Parada paradaInicial = pas.encontrarPorId(idParada);
                                if (paradaInicial != null) {
                                    Credenciales nuevasCredenciales = new Credenciales(usuario, contraseña, TipoUsuario.PEREGRINO);
                                    nuevasCredenciales = cs.guardar(nuevasCredenciales);

                                    Peregrino nuevoPeregrino = new Peregrino(nuevasCredenciales.getId(), nombre, nacionalidad);
                                    nuevoPeregrino = pes.guardar(nuevoPeregrino);

                                    Carnet nuevoCarnet = new Carnet(nuevoPeregrino.getId()); //NOOO TOCAR POR NADA DEL MUNDO
                                    nuevoCarnet.setParadaInicial(paradaInicial);
                                    nuevoCarnet = cas.guardar(nuevoCarnet);

                                    pps.guardarPeregrinoParada(nuevoPeregrino.getId(), nuevoCarnet.getParadaInicial().getId());

                                    Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
                                    confirmacion.setTitle("Operación exitosa");
                                    confirmacion.setHeaderText("Se ha registrado el usuario y la parada exitosamente");
                                    confirmacion.showAndWait();

                                } else {
                                    Alert error = new Alert(Alert.AlertType.ERROR);
                                    error.setTitle("Error");
                                    error.setHeaderText("Parada inicial inválida");
                                    error.setContentText("La parada inicial no ha sido encontrada en la base de datos");
                                    error.showAndWait();
                                }

                            } else {
                                Alert error = new Alert(Alert.AlertType.ERROR);
                                error.setTitle("Error");
                                error.setHeaderText("Parada inicial inválida");
                                error.setContentText("La parada inicial no puede estar vacia");
                                error.showAndWait();
                            }

                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("Nacionalidad inválida");
                            error.setContentText("La nacionalidad no puede estar vacia");
                            error.showAndWait();
                        }

                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("Nombre inválido");
                        error.setContentText("El nombre no puede estar vacio");
                        error.showAndWait();
                    }

                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Contraseña inválida");
                    error.setContentText("Debe contener números, letras, símbolos especiales como _, ! o ? y una longitud de 8 caracteres");
                    error.showAndWait();

                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Nombre de usuario inválido");
                error.setContentText("El usuario solo puede tener números, letras y guión bajo");
                error.showAndWait();
            }

        } catch(Exception e) {
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
        cambiarPanelIniciarSesion();

        List<String> paisesNacionalidad = leerPaisesXML();
        cbNacionalidad.getItems().addAll(paisesNacionalidad);

        List<Parada> listaParadas = pas.encontrarTodos();
        ObservableList<String> elementosParada = FXCollections.observableArrayList();

        for(Parada indice: listaParadas) {
            String campos = indice.getId() + " - " + indice.getNombre() + " - " + indice.getRegion() + " - " +indice.getResponsable();
            elementosParada.add(campos);
        }

        cbParadaInicial.getItems().addAll(elementosParada);
    }

    /***
     * Método validarNombre que obtiene una cadena de caracteres y se asegura, mediante el uso
     * de una expresión regular muy sencilla, que tenga los caracteres permitidos. Además, verifica que
     * la cadena no esté vacía o solo contenga espacios en blanco.
     *
     * @param nombre con la cadena de caracteres que se quiere validar
     * @return true si pasa la validación, false sino.
     */
    private boolean validarNombre(String nombre) {
        if(nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            String nombreSinEspacios = nombre.trim();
            if(!nombreSinEspacios.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /***
     * Método leerPaisesXML que utiliza la tecnología DOM para leer un fichero del tipo XML y extraer sus datos,
     * almacenándolos en una lista que se utilizará posteriormente para poder registrar un peregrino nuevo.
     *
     * @return listaPaisesXML con los países que contenía el archivo XML.
     */
    private List<String> leerPaisesXML() {
        List<String> listaPaisesXML = new ArrayList<>();

        try {
            File archivoXML = new File("Tarea4ADSophiaDeLucaMiranda/src/main/resources/paises.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.parse(archivoXML);

            NodeList listaPaises = documento.getElementsByTagName("pais");

            for(int i = 0; i < listaPaises.getLength(); i++) {
                Element elementoPais = (Element) listaPaises.item(i);

                String idPais = elementoPais.getElementsByTagName("id").item(0).getTextContent();
                String nombrePais = elementoPais.getElementsByTagName("nombre").item(0).getTextContent();

                listaPaisesXML.add(idPais + " - " + nombrePais);
            }

        } catch (ParserConfigurationException pce) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción del tipo ParserConfigurationException");
            error.setContentText(pce.getMessage());
            error.showAndWait();

        } catch (IOException ioe) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción del tipo IOException");
            error.setContentText(ioe.getMessage());
            error.showAndWait();

        } catch (SAXException saxe) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción del tipo SAXException");
            error.setContentText(saxe.getMessage());
            error.showAndWait();
        }

        return listaPaisesXML;
    }
}