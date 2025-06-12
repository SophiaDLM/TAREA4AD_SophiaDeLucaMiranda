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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class EstanciaControlador implements Initializable {
    //Elementos relacionados con el archivo FXML:
    @FXML
    private GridPane pnlServicio;

    @FXML
    private ListView<String> lstServicios;

    @FXML
    private ChoiceBox<String> cbPago;

    @FXML
    private TextArea taExtra;

    @FXML
    private TextField tfTotal;

    @FXML
    private GridPane pnlEnvio;

    @FXML
    private TextField tfDireccion;

    @FXML
    private TextField tfLocalidad;

    @FXML
    private TextField tfPeso;

    @FXML
    private TextField tfVolumen;

    @FXML
    private CheckBox chbUrgente;

    @FXML
    private TextField tfConjunto;

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
    private EstanciaServicio es;

    @Autowired
    private ServicioServicio ss;

    @Autowired
    private ConjuntoContratadoServicio ccs;

    @Autowired
    private EnvioACasaServicio eacs;

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
     *
     */
    @FXML
    public void cambiarPanelConjunto() {
        pnlServicio.setVisible(true);
        pnlEnvio.setVisible(false);
    }

    /***
     *
     */
    @FXML
    public void cambiarPanelEnvio() {
        pnlServicio.setVisible(false);
        pnlEnvio.setVisible(true);
    }

    /***
     *
     */
    @FXML
    public void contratarServicio() {
        List<ConjuntoContratado> listaConjuntos;

        try {
            Parada paradaActual = obtenerParada();
            List<Long> idServicios = obtenerListaServicios();

            String pagoSeleccionado = cbPago.getSelectionModel().getSelectedItem();
            if(pagoSeleccionado != null || !pagoSeleccionado.isEmpty()) {
                char modoPago = pagoSeleccionado.charAt(0);
                String extra = taExtra.getText();
                double precioTotal = Double.parseDouble(tfTotal.getText());
                Long idEstancia = es.encontrarPorIdParadaEnOrden(paradaActual.getId()).getId();

                listaConjuntos = obtenerListaConjuntos();
                Long idNuevo = listaConjuntos.isEmpty() ?1 : listaConjuntos.getLast().getId() + 1;

                ConjuntoContratado conjunto = new ConjuntoContratado(idNuevo, precioTotal, modoPago, extra);
                conjunto.setIdServicios(idServicios);
                conjunto.setIdEstancia(idEstancia);
                ccs.guardar(conjunto);

                System.out.println(conjunto.toString());

                if(contieneEnvioACasa) {
                    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion.setTitle("Confirmar");
                    confirmacion.setHeaderText("¿Está seguro de que desea contratar el servicio especial de envío a casa?");

                    ButtonType confirmar = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

                    if (confirmar == ButtonType.OK) {
                        cambiarPanelEnvio();
                    }

                } else {
                    //EVITAR CONTRATAR EL SERVICIO - CONTROLAR MEJOR ESTO
                    Alert informacion = new Alert(Alert.AlertType.INFORMATION);
                    informacion.setTitle("Operación exitosa");
                    informacion.setHeaderText("Se han contratado los servicios exitosamente");
                    informacion.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Debe seleccionar un método de pago");
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

    /***
     *
     */
    @FXML
    public void registrarEnvioACasa() {
        Parada paradaActual = obtenerParada();
        String direccion = tfDireccion.getText();
        String localidad = tfLocalidad.getText();
        String textoPeso = tfPeso.getText();
        String textoVolumen = tfVolumen.getText();

        try {
            if(direccion != null || !direccion.isEmpty()) {
                if(localidad != null || !localidad.isEmpty()) {
                    if(textoPeso.matches("^\\d+(\\.\\d+)?$")) {
                        double peso = Double.parseDouble(textoPeso);

                        String[] partesVolumen = textoVolumen.split("-");
                        if(partesVolumen.length == 3 ) {
                            int[] volumen = new int[3];

                            for(int i = 0; i < 3; i++) {
                                volumen[i] = Integer.parseInt(partesVolumen[i]);
                            }

                            boolean urgente = chbUrgente.isSelected();

                            //Crear objeto Dirección
                            Direccion direccionEnvio = new Direccion(direccion, localidad);

                            //Crear objeto Envío a Casa
                            EnvioACasa envio = new EnvioACasa(peso, volumen, urgente);
                            envio.setDireccion(direccionEnvio);
                            envio.setIdParada(paradaActual.getId());
                            eacs.guardar(envio);

                            Alert informacion = new Alert(Alert.AlertType.INFORMATION);
                            informacion.setTitle("Operación exitosa");
                            informacion.setHeaderText("Se han contratado los servicios exitosamente");
                            informacion.showAndWait();

                            System.out.println(envio.toString());

                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("El volumen debe introducirse de la siguiente manera: '##-##-##', es decir, largo-ancho-alto");
                            error.showAndWait();
                        }

                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("El peso debe introducirse como decimal, es decir, ##.##");
                        error.showAndWait();
                    }

                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("La localidad no puede estar vacía");
                    error.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("La dirección no puede estar vacía");
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

    /***
     * Método initialize que sirve para cargar valores al arrancar la aplicación.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Long idParada = obtenerParada().getId();
        List<Servicio> serviciosParada = ss.encontrarPorIdParada(idParada);
        ObservableList<String> auxiliar = FXCollections.observableArrayList();

        for(Servicio indice: serviciosParada) {
            auxiliar.add(indice.getId() + " - " + indice.getNombre() + " - " + indice.getPrecio());
        }

        lstServicios.setItems(auxiliar);
        lstServicios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cbPago.setItems(FXCollections.observableArrayList("Efectivo (E)", "Tarjeta (T)", "Bizum (B)"));

        lstServicios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            calcularTotalPrecio(serviciosParada);
        });

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

        if (credencialesActuales != null) {
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
     *
     */
    private void calcularTotalPrecio(List<Servicio> servicios) {
        ObservableList<String> serviciosSeleccionados = lstServicios.getSelectionModel().getSelectedItems();
        double total = 0;

        for (String indice : serviciosSeleccionados) {
            String nombre = indice.split(" - ")[1];
            for (Servicio indice2 : servicios) {
                if (indice2.getNombre().equals(nombre)) {
                    total += indice2.getPrecio();
                    break;
                }
            }
        }

        tfTotal.setText(String.valueOf(total));
    }

    private boolean contieneEnvioACasa = false;

    private List<Long> obtenerListaServicios() {
        List<String> listaServiciosString = lstServicios.getSelectionModel().getSelectedItems();
        contieneEnvioACasa = false;

        if(!listaServiciosString.isEmpty()) {
            List<Long> listaIdServicios = new ArrayList<>();

            for (String indice : listaServiciosString) {
                String[] campos = indice.split(" - ");
                Long idServicio = Long.parseLong(campos[0]);
                listaIdServicios.add(idServicio);

                //Verifica si está con o sin acento
                if(campos.length > 1 && campos[1].toLowerCase().contains("envío a casa") || campos.length > 1 && campos[1].toLowerCase().contains("envio a casa")) {
                    contieneEnvioACasa = true;
                }
            }
            return listaIdServicios;

        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Debe seleccionar al menos un servicio");
            error.showAndWait();
        }

        return null;
    }

    private List<ConjuntoContratado> obtenerListaConjuntos() {
        return ccs.encontrarTodos();
    }
}