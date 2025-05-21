package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.controlador;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config.ManejadorEscenas;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.*;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.*;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista.VistaFxml;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Clase ParadaControlador que se encarga de manejar las acciones
 * disponibles de un administrador de parada como lo es acceder a la ayuda de usuario
 * (no implementada aún), al método de exportar datos de la parada o sellar el carnet de un peregrino
 * y ofrecerle un alojamiento, y a cerrar sesión si así lo desea.
 *
 * Esta clase implementa Initializable para el uso de JavaFX.
 */
@Controller
public class ParadaControlador implements Initializable {
    //Elementos relacionados con el archivo FXML:
    @FXML
    private GridPane panelParada;

    @FXML
    private GridPane panelExportar;

    @FXML
    private GridPane panelSellarAlojarse;

//    @FXML
//    private GridPane panelEnvios;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfId1;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfNombre1;

    @FXML
    private TextField tfRegion;

    @FXML
    private TextField tfRegion1;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private TableView<Estancia> tvEstancias;

    @FXML
    private TableColumn<Estancia, Long> tcId;

    @FXML
    private TableColumn<Estancia, String> tcPeregrino;

    @FXML
    private TableColumn<Estancia, LocalDate> tcFecha;

    @FXML
    private TableColumn<Estancia, Boolean> tcVip;

    private ObservableList<Estancia> listaEstancias;

    @FXML
    private ChoiceBox<String> cbPeregrino;

    @FXML
    private TextField tfNombrePeregrino;

    @FXML
    private TextField tfNacionalidadPeregrino;

    @FXML
    private Button btnAlojarse;

    @FXML
    private Button btnAlojarseVIP;

//    @FXML
//    private ListView<Servicio> lstServicios;
//
//    @FXML
//    private ChoiceBox<String> cbPago;
//
//    @FXML
//    private TextField tfExtra;
//
//    @FXML
//    private TextField tfTotal;

//    @FXML
//    private TableView<EnvioACasa> tbEnvios;
//
//    @FXML
//    private TableColumn<EnvioACasa, Long> tcIdE;
//
//    @FXML
//    private TableColumn<EnvioACasa, Double> tcPeso;
//
//    @FXML
//    private TableColumn<EnvioACasa, String> tcVolumen;
//
//    @FXML
//    private TableColumn<EnvioACasa, Boolean> tcUrgente;
//
//    @FXML
//    private TableColumn<EnvioACasa, String> tcDireccion;
//
//    @FXML
//    private TableColumn<EnvioACasa, String> tcLocalidad;

//    @FXML
//    private TextField tfDireccion;
//
//    @FXML
//    private TextField tfLocalidad;

    //Elementos relacionados con el manejo de las escenas:
    @Lazy
    @Autowired
    private ManejadorEscenas me;

    //Elementos relacionados con la manipulación de la base de datos:
    @Autowired
    private SesionUsuario su;

    @Autowired
    private ParadaServicio pas;

    @Autowired
    private PeregrinoServicio pes;

    @Autowired
    private EstanciaServicio ess;

    @Autowired
    private CarnetServicio cas;

    @Autowired
    private PeregrinoParadaServicio pps;

    /***
     * Método cerrarSesion que lanza una alerta pidiendo al usuario que
     * confirme su decisión. Si el usuario pulsa en el botón de aceptar, se
     * camibia la ventana a la de INICIARSESION y se asignan las credenciales
     * a null para indicar que el usuario ha vuelto a ser un invitado.
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
     * Método cambiarPanelParada que cambia la visibilidad de los paneles.
     */
    @FXML
    public void cambiarPanelParada() {
        panelParada.setVisible(true);
        panelExportar.setVisible(false);
        panelSellarAlojarse.setVisible(false);
        //panelEnvios.setVisible(false);
    }

    /***
     * Método cambiarPanelExportar que cambia la visibilidad de los paneles.
     */
    @FXML
    public void cambiarPanelExportar() {
        panelParada.setVisible(false);
        panelExportar.setVisible(true);
        panelSellarAlojarse.setVisible(false);
        //panelEnvios.setVisible(false);
    }

    /***
     * Método cambiarPanelSellarAlojarse que cambia la visibilidad de los paneles.
     */
    @FXML
    public void cambiarPanelSellarAlojarse() {
        panelParada.setVisible(false);
        panelExportar.setVisible(false);
        panelSellarAlojarse.setVisible(true);
        //panelEnvios.setVisible(false);
    }

//    @FXML
//    public void cambiarPanelEnvios() {
//        panelParada.setVisible(false);
//        panelExportar.setVisible(false);
//        panelSellarAlojarse.setVisible(false);
//        panelEnvios.setVisible(true);
//    }

    /***
     * Método exportarDatosParada que obtiene y muestra los datos básicos de una parada, pide un rango de fechas y lo valida.
     * Si el rango es válido, muestra una tabla con todas las estancias realizadas en esa parada en ese rango de fechas,
     * si el rango no es válido, debería lanzar una alerta de error.
     */
    @FXML
    public void exportarDatosParada() {
        try {
            Parada paradaActual = obtenerParada();

            if (validarRangoFechas(dpFechaInicio.getValue(), dpFechaFin.getValue())) {
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar Datos");
                confirmacion.setHeaderText("¿Son los datos correctos?");

                ButtonType confirmar = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

                if (confirmar == ButtonType.OK) {
                    List<Estancia> estanciasEncontradas = obtenerEstancias(paradaActual.getId(), dpFechaInicio.getValue(), dpFechaFin.getValue());

                    listaEstancias.clear();
                    listaEstancias.addAll(estanciasEncontradas);
                    tvEstancias.refresh();

                } else {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Modificar Datos");
                    info.setHeaderText("Modifique los datos para que sean correctos");
                    info.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("¡Oops!");
                error.setContentText("El rango de fechas no es correcto");
                error.showAndWait();
            }

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    /*** EDITAR
     * Método sellarAlojarse que muestra la información básica de una parada, pide el ID de un peregrino y
     * muestra sus datos básicos para luego perdirle confirmación y sellar su carnet. Luego se pregunta si el usuario desea
     * realizar una estancia y, si es el caso, si desea que sea del tipo V.I.P.
     * La mayor parte del proceso se realiza mediante alertas, lo cual puede resultar molesto o incluso como mala
     * práctica, por lo que se intentará encontrar una forma mejor de implementarlo. Sin embargo, después de un
     * par de pruebas, se ha determinado que los cambios sí que se realizan en la base de datos.
     */
    Parada paradaActual;
    Long idPeregrino;
    Carnet actualizarCarnet;

    @FXML
    public void sellarCarnet() {
        paradaActual = obtenerParada();
        idPeregrino = Long.parseLong(cbPeregrino.getSelectionModel().getSelectedItem());
        actualizarCarnet = obtenerCarnet(idPeregrino);
        LocalDateTime fechaHora = LocalDateTime.now();

        try {
            if (actualizarCarnet.getPeregrino().getId() == actualizarCarnet.getId() && actualizarCarnet.getParadaInicial().getId() == paradaActual.getId()) {
                tfNombrePeregrino.setText(actualizarCarnet.getPeregrino().getNombre());
                tfNacionalidadPeregrino.setText(actualizarCarnet.getPeregrino().getNacionalidad());

                Thread.sleep(500);

                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar Datos");
                confirmacion.setHeaderText("¿Son los datos correctos?");

                ButtonType confirmar = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

                if (confirmar == ButtonType.OK) {
                    actualizarCarnet.setDistancia(actualizarCarnet.getDistancia() + 10);
                    cas.actualizar(actualizarCarnet);

                    if (!verificarPasoPeregrinoParada(idPeregrino, paradaActual.getId(), fechaHora)) {
                        pps.guardarPeregrinoParada(idPeregrino, paradaActual.getId(), fechaHora);
                    }

                    Alert actualizado = new Alert(Alert.AlertType.INFORMATION);
                    actualizado.setTitle("Operación exitosa");
                    actualizado.setHeaderText("Se ha actualizado el carnet");
                    actualizado.showAndWait();

                    Alert confirmacion2 = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion2.setTitle("Confirmar");
                    confirmacion2.setHeaderText("¿Desea alojarse?");

                    ButtonType confirmar2 = confirmacion2.showAndWait().orElse(ButtonType.CANCEL);

                    if (confirmar2 == ButtonType.OK) {
                        btnAlojarse.setDisable(false);
                        btnAlojarseVIP.setDisable(false);
                    }

                } else {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Modificar Datos");
                    info.setHeaderText("Modifique los datos para que sean correctos");
                    info.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("¡Oops!");
                error.setContentText("El peregrino no ha pasado por esta parada");
                error.showAndWait();
            }

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general (es posible que el ID del peregrino no haya sido encontrado)");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    //FUNCIONA
    @FXML
    public void alojarEstandar() {
        paradaActual = obtenerParada();
        idPeregrino = Long.parseLong(cbPeregrino.getSelectionModel().getSelectedItem());
        actualizarCarnet = obtenerCarnet(idPeregrino);

        try {
            Estancia nuevaEstancia = new Estancia(LocalDate.now(), false);
            nuevaEstancia.setPeregrino(actualizarCarnet.getPeregrino());
            nuevaEstancia.setParada(paradaActual);
            ess.guardar(nuevaEstancia);

            Alert actualizado1 = new Alert(Alert.AlertType.INFORMATION);
            actualizado1.setTitle("Operación exitosa");
            actualizado1.setHeaderText("Se ha registrado la estancia");
            actualizado1.showAndWait();

            Thread.sleep(500);

            Alert confirmacion3 = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion3.setTitle("Confirmar");
            confirmacion3.setHeaderText("¿Desea contratar un servicio?");

            ButtonType confirmar3 = confirmacion3.showAndWait().orElse(ButtonType.CANCEL);

            if (confirmar3 == ButtonType.OK) {
                me.cambiarEscena(VistaFxml.ESTANCIA);
            }
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    //FUNCIONA
    @FXML
    public void alojarVip() {
        paradaActual = obtenerParada();
        idPeregrino = Long.parseLong(cbPeregrino.getSelectionModel().getSelectedItem());
        actualizarCarnet = obtenerCarnet(idPeregrino);

        try {
            Estancia nuevaEstancia = new Estancia(LocalDate.now(), true);
            nuevaEstancia.setPeregrino(actualizarCarnet.getPeregrino());
            nuevaEstancia.setParada(paradaActual);
            ess.guardar(nuevaEstancia);

            actualizarCarnet.setNvips(actualizarCarnet.getNvips() + 1);
            cas.actualizar(actualizarCarnet);

            Alert actualizado1 = new Alert(Alert.AlertType.INFORMATION);
            actualizado1.setTitle("Operación exitosa");
            actualizado1.setHeaderText("Se ha registrado la estancia V.I.P. y se ha actualizado el carnet");
            actualizado1.showAndWait();

            Thread.sleep(500);

            Alert confirmacion3 = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion3.setTitle("Confirmar");
            confirmacion3.setHeaderText("¿Desea contratar un servicio?");

            ButtonType confirmar3 = confirmacion3.showAndWait().orElse(ButtonType.CANCEL);

            if (confirmar3 == ButtonType.OK) {
                me.cambiarEscena(VistaFxml.ESTANCIA);
            }
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción general");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    //METODO VER ENVIOS REALIZADOS
    public void verEnviosRealizados() {
//        try {
//            cambiarPanelEnvios();
//            List<EnvioACasa> enviosRealizados = eacs.encontrarTodos();
//
//            ObservableList<EnvioACasa> enviosListaObservable = FXCollections.observableArrayList(enviosRealizados);
//
//            tcIdE.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
//            tcPeso.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPeso()));
//            tcVolumen.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVolumenString()));
//            tcUrgente.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isUrgente()));
//            tcDireccion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDireccion().getDireccion()));
//            tcLocalidad.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDireccion().getLocalidad()));
//
//            tbEnvios.setItems(enviosListaObservable);
//
//        } catch (Exception e) {
//            Alert error = new Alert(Alert.AlertType.ERROR);
//            error.setTitle("Fatal Error");
//            error.setHeaderText("Ocurrió una excepción general (es posible que el ID del peregrino no haya sido encontrado)");
//            error.setContentText(e.getMessage());
//            error.showAndWait();
//
//        }
    }

    /***
     * Método initialize que sirve para cargar valores al arrancar la aplicación.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cambiarPanelParada();

        Parada paradaActual = obtenerParada();

        tfId.setText(String.valueOf(paradaActual.getId()));
        tfNombre.setText(paradaActual.getNombre());
        tfRegion.setText(paradaActual.getRegion()+"");

        List<Peregrino> listaPeregrinos = pes.encontrarTodos();
        ObservableList<String> elementosPeregrino = FXCollections.observableArrayList();

        for(Peregrino indice: listaPeregrinos) {
            String campos = String.valueOf(indice.getId());
            elementosPeregrino.add(campos);
        }

        cbPeregrino.getItems().addAll(elementosPeregrino);

        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcPeregrino.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeregrino().getNombre()));
        tcFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tcVip.setCellValueFactory(new PropertyValueFactory<>("vip"));

        listaEstancias = FXCollections.observableArrayList();
        tvEstancias.setItems(listaEstancias);

        tfId1.setText(String.valueOf(paradaActual.getId()));
        tfNombre1.setText(paradaActual.getNombre());
        tfRegion1.setText(paradaActual.getRegion()+"");
    }

    /***
     * Método obtenerParada que sirve para obtener el objeto parada de la base
     * de datos accediendo a él gracias a la sesión guardada al iniciar sesión.
     *
     * @return paradaActual si se encontró la parada, sino, debería lanzar un error.
     */
    private Parada obtenerParada() {
        Credenciales credencialesActuales = su.getCredenciales();
        Parada paradaActual = null;

        if(credencialesActuales != null) {
            paradaActual = pas.encontrarPorId(credencialesActuales.getId());
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Fatal error");
            error.setContentText("No se encontro el peregrino");
        }
        return paradaActual;
    }


    /***
     * Método validarRangoFechas que valida si un rango es válido, es decir, que la fecha de inicio
     * no sea mayor que la de fin y que la de fin no sea menor que la de inicio ni mayor que la de hoy.
     *
     * @param fecha1
     * @param fecha2
     * @return true si el rango es válido, false sino.
     */
    private boolean validarRangoFechas(LocalDate fecha1, LocalDate fecha2) {
        // MAL HECHO
        fecha1 = dpFechaInicio.getValue();
        fecha2 = dpFechaFin.getValue();

        if(!fecha1.isAfter(LocalDate.now()) || !fecha1.isAfter(fecha2) || !fecha2.isAfter(LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * Método obtenerEstancias que sirve para obtener una lista de todas las estancias asociadas
     * a un parada.
     *
     * @param idParada
     * @return la lista de estanciasActuales.
     */
    private List<Estancia> obtenerEstancias(Long idParada, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Estancia> estanciasActuales = ess.encontrarPorIdParadaYRangoFecha(idParada, fechaInicio, fechaFin);
        return estanciasActuales;
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

    private boolean verificarPasoPeregrinoParada(Long idPeregrino, Long idParada, LocalDateTime fechaHora) {
        if(pps.existePeregrinoParada(idPeregrino, idParada, fechaHora) != 0) {
            return true;
        }
        return false;
    }
}