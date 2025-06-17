package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.controlador;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config.ManejadorEscenas;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexionExistDB;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexionMongoDB;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Credenciales;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Parada;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Servicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.TipoUsuario;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.CredencialesServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.ParadaServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.ServicioServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista.VistaFxml;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Clase AdministradorControlador que se encarga de manejar las acciones
 * disponibles de un administrador como lo es acceder a la ayuda de usuario
 * (no implementada aún), al método para añadir una nueva parada a la base de datos
 * y a cerrar sesión si así lo desea.
 * Esta clase implementa Initializable para el uso de JavaFX.
 */
@Controller
public class AdministradorControlador implements Initializable {
    //Elementos relacionados con el archivo FXML:
    @FXML
    private GridPane pnlAdministrador;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfRegion;

    @FXML
    private TextField tfResponsable;

    @FXML
    private TextField tfUsuario;

    @FXML
    private PasswordField pfContraseña;

    @FXML
    private GridPane pnlNuevaParada;

    @FXML
    private TextField tfNombreServicio;

    @FXML
    private TextField tfPrecioServicio;

    @FXML
    private TableView<Long> tvParadasVer;

    @FXML
    private TableColumn<Long, Long> tcParadasVer;

    @FXML
    private TableView<Long> tvParadasUsar;

    @FXML
    private TableColumn<Long, Long> tcParadasUsar;

    @FXML
    private GridPane pnlNuevoServicio;

    @FXML
    private GridPane pnlEditarServicio;

    @FXML
    private Button btnCrear;

    @FXML
    private Button btnGuardar;

    @FXML
    private TableView<Servicio> tvServiciosEditar;

    @FXML
    private TableColumn<Servicio, Long> tcIdServicio;

    @FXML
    private TableColumn<Servicio, String> tcNombreServicio;

    @FXML
    private TableColumn<Servicio, Double> tcPrecioServicio;

    @FXML
    private TableColumn<Servicio, String> tcParadasServicio;

    //Elementos relacionados con el manejo de las escenas:
    @Lazy
    @Autowired
    private ManejadorEscenas me;

    //Elementos relacionados con la manipulación de la base de datos:
    @Autowired
    private CredencialesServicio cs;

    @Autowired
    private ParadaServicio ps;

    @Autowired
    private ServicioServicio svs;

    private DataConexionExistDB existDB;

    ObservableList<Servicio> lstServicioEditar;

    @Autowired
    private DataConexionMongoDB mongoDB;

    //Métodos:
    @FXML
    private void cambiarPanelNuevaParada() {
        pnlNuevaParada.setVisible(true);
        pnlNuevoServicio.setVisible(false);
        pnlEditarServicio.setVisible(false);
        pnlAdministrador.setVisible(false);
    }

    @FXML
    private void cambiarPanelNuevoServicio() {
        pnlNuevaParada.setVisible(false);
        pnlNuevoServicio.setVisible(true);
        pnlEditarServicio.setVisible(false);
        pnlAdministrador.setVisible(false);
        tvServiciosEditar.refresh();
    }

    @FXML
    private void cambiarPanelEditarServicio() {
        pnlNuevaParada.setVisible(false);
        pnlNuevoServicio.setVisible(false);
        pnlEditarServicio.setVisible(true);
        pnlAdministrador.setVisible(false);
        tvServiciosEditar.refresh();
    }

    @FXML
    private void cambiarPanel() {
        cambiarPanelNuevoServicio();
        btnGuardar.setVisible(true);
        btnCrear.setVisible(false);
        tvServiciosEditar.refresh();
        cargarServicio();
    }

    /***
     * Método cerrarSesion que lanza una alerta pidiendo al usuario que
     * confirme su decisión. Si el usuario pulsa en el botón de aceptar, se
     * cambia la ventana a la de INICIARSESION.
     * En este método no se maneja en sí la sesión del usuario, puesto que
     * sólo hay un administrador general y en ninguno de los casos de uso se
     * especifica que se deba mostrar información suya en pantalla (Además de
     * que esto supondría un grave problema en cuanto a seguridad).
     */
    @FXML
    public void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación Cerrar Sesión");
        confirmacion.setContentText("¿Está seguro que desea cerrar sesión?");

        ButtonType confirmar = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

        if (confirmar == ButtonType.OK) {
            me.cambiarEscena(VistaFxml.INICIARSESION);
        }
    }

    @FXML
    public void volver() {
        pnlNuevaParada.setVisible(false);
        pnlNuevoServicio.setVisible(false);
        pnlEditarServicio.setVisible(false);
        pnlAdministrador.setVisible(true);

        //Limpiar tabla servicios
        tvServiciosEditar.getItems().clear();
        lstServicioEditar = FXCollections.observableArrayList(obtenerListaServicios());
        tvServiciosEditar.setItems(lstServicioEditar);
        tvServiciosEditar.refresh();

        //Limpiar formulario servicio
        tvParadasUsar.getItems().clear();
    }

    /***
     * Método nuevaParada que obtiene los datos de los campos en la interfaz gráfica, los valida y posteriormente
     * los registra en la base de datos, lanzándo una alerta si todo ha salido bien.
     */
    @FXML
    public void nuevaParada() {
        try {
            String nombre = tfNombre.getText();
            char region = tfRegion.getText().charAt(0);
            String responsable = tfResponsable.getText();
            String usuario = tfUsuario.getText();
            String contraseña = pfContraseña.getText();

            if (validarNombre(nombre)) {
                if (ps.encontrarPorNombre(nombre) == null) {
                    if (Character.isLetter(region)) {
                        if (validarNombre(responsable)) {
                            if (usuario.matches("[a-zA-Z0-9_]+")) {
                                if (cs.encontrarPorNombreUsuario(usuario) == null) {
                                    if (contraseña.matches("[a-zA-Z0-9_]{8}")) {
                                        Credenciales nuevasCredenciales = new Credenciales(usuario, contraseña, TipoUsuario.PARADA);
                                        nuevasCredenciales = cs.guardar(nuevasCredenciales);

                                        Parada nuevaParada = new Parada(nuevasCredenciales.getId(), nombre, region, responsable);
                                        nuevaParada = ps.guardar(nuevaParada);

                                        existDB = new DataConexionExistDB();

                                        String nombreSinEspacios = nombre.replaceAll("\\s+", "");
                                        existDB.crearSubColeccionParada("parada_" + nombreSinEspacios);

                                        Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
                                        confirmacion.setTitle("Operación Exitosa");
                                        confirmacion.setHeaderText("Se ha registrado el usuario y la parada exitosamente");
                                        confirmacion.showAndWait();

                                        tfNombre.setText("");
                                        tfRegion.setText("");
                                        tfResponsable.setText("");
                                        tfUsuario.setText("");
                                        pfContraseña.setText("");

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
                                    error.setHeaderText("Ese usuario ya existe");
                                    error.showAndWait();
                                }

                            } else {
                                Alert error = new Alert(Alert.AlertType.ERROR);
                                error.setTitle("Error");
                                error.setHeaderText("Nombre de usuario inválido");
                                error.setContentText("El usuario solo puede tener números, letras y guión bajo");
                                error.showAndWait();
                            }

                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("Nombre del responsable inválido");
                            error.setContentText("El nombre no puede estar vacio o contener caracteres que no sean letras");
                            error.showAndWait();
                        }

                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("Región inválida");
                        error.setContentText("La región solo puede ser una letra");
                        error.showAndWait();
                    }

                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Esa parada ya existe");
                    error.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Nombre inválido");
                error.setContentText("El nombre no puede estar vacio o contener caracteres que no sean letras");
                error.showAndWait();
            }

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción desconocida");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    public void nuevoServicio() {
        try {
            String nombreServicio = tfNombreServicio.getText();
            String precioServicio = tfPrecioServicio.getText();
            List<Long> listaIdParadas = new ArrayList<>(tvParadasUsar.getItems());
            List<Servicio> listaServicios;

            if (validarNombre(nombreServicio)) {
                if (svs.encontrarPorNombre(nombreServicio) == null) {
                    if (precioServicio.matches("^\\d+(\\.\\d{1,2})?$")) {
                        listaServicios = obtenerListaServicios();
                        Long idNuevo = listaServicios.isEmpty() ? 1 : listaServicios.getLast().getId() + 1;

                        Servicio servicio = new Servicio(idNuevo, nombreServicio, Double.parseDouble(precioServicio));
                        servicio.setIdParadas(listaIdParadas);

                        if (svs.guardar(servicio)) {
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setTitle("Servicio añadido");
                            alerta.setHeaderText("El servicio ha sido añadido correctamente");
                            alerta.showAndWait();

                            tfNombreServicio.setText("");
                            tfPrecioServicio.setText("");
                            tvParadasUsar.getItems().clear();

                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("Servicio no añadido");
                            error.setContentText("El servicio no se ha podido añadir por algún error interno");
                            error.showAndWait();
                        }

                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("Precio inválido");
                        error.setContentText("El formato correcto es xx.xx, por ejemplo, 1500.99");
                        error.showAndWait();
                    }

                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Servicio existente");
                    error.setContentText("No se ha podido añadir el nuevo servicio porque ya existe");
                    error.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Nombre inválido");
                error.setContentText("El nombre no puede estar vació o contener caracteres que no sean letras");
                error.showAndWait();
            }

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción desconocida");
            error.setContentText(e.getMessage());
            error.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void añadirParadaALista() {
        Long idSeleccionado = tvParadasVer.getSelectionModel().getSelectedItem();

        if (idSeleccionado != null) {
            tvParadasUsar.getItems().add(idSeleccionado);
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("No se ha seleccionado ningún ID de parada");
            error.showAndWait();
        }
    }

    @FXML
    private void borrarParadaDeLista() {
        Long idSeleccionado = tvParadasUsar.getSelectionModel().getSelectedItem();

        if (idSeleccionado != null) {
            tvParadasUsar.getItems().remove(idSeleccionado);
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("No se ha seleccionado ningún ID de parada");
            error.showAndWait();
        }
    }

    @FXML
    public void editarServicio() {
        try {
            String editarServicio = tfNombreServicio.getText();
            String editarPrecioServicio = tfPrecioServicio.getText();
            List<Long> editarListaIdParadas = new ArrayList<>(tcParadasUsar.getTableView().getItems());

            if (validarNombre(editarServicio)) {
                if (editarPrecioServicio.matches("^\\d+(\\.\\d{1,2})?$")) {
                    Servicio servicio = cargarServicio();
                    servicio.setNombre(editarServicio);
                    servicio.setPrecio(Double.parseDouble(editarPrecioServicio));
                    servicio.setIdParadas(editarListaIdParadas);
                    svs.actualizar(servicio);

                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Servicio editado");
                    alerta.setHeaderText("El servicio ha sido editado correctamente");
                    alerta.showAndWait();

                    tfNombreServicio.setText("");
                    tfPrecioServicio.setText("");
                    tvParadasUsar.getItems().clear();

                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Precio inválido");
                    error.setContentText("No pueden introducirse letras ni caracteres especiales a excepción de: \".\", \",\" y \"€\"");
                    error.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Nombre inválido");
                error.setContentText("El nombre no puede estar vacio o contener caracteres que no sean letras");
                error.showAndWait();
            }

//            } else {
//                Alert error = new Alert(Alert.AlertType.ERROR);
//                error.setTitle("Error");
//                error.setHeaderText("Servicio no seleccionado");
//                error.setContentText("Por favor, seleccione un servicio válido");
//                error.showAndWait();
//            }

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción desconocida");
            error.setContentText(e.getMessage());
            error.showAndWait();
            e.printStackTrace();
        }
    }

    /***
     *
     */
    public void hacerBackupCarnets() {
        try {
            mongoDB.crearBackupCarnets();

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fatal Error");
            error.setHeaderText("Ocurrió una excepción desconocida");
            error.setContentText(e.getMessage());
            error.showAndWait();
            e.printStackTrace();
        }
    }

    /***
     * Método initialize que no ha sido utilizado porque en este caso no es
     * necesario, pero es obligatorio implementarlo. En casos en los que se necesite
     * cargar algún dato en la pantalla apenas arranque la aplicación, sería
     * dentro de este método que se colocaría.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcParadasVer.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue()));
        tvParadasVer.setItems(obtenerIdParadas());

        tcParadasUsar.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue()));

        tcIdServicio.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreServicio.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcPrecioServicio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcParadasServicio.setCellValueFactory(new PropertyValueFactory<>("idParadasString"));

        lstServicioEditar = FXCollections.observableArrayList(obtenerListaServicios());
        tvServiciosEditar.setItems(lstServicioEditar);

        btnCrear.setVisible(true);
        btnGuardar.setVisible(false);
    }

    /***
     * Método validarNombre que obtiene una cadena de caracteres y se asegura, mediante el uso
     * de una expresión regular muy sencilla, que tenga los caracteres permitidos. Además, verifica que
     * la cadena no esté vacía o solo contenga espacios en blanco.
     *
     * @param nombre con la cadena de caracteres que se quiere validar
     * @return true si pasa la validación, false si no.
     */
    private boolean validarNombre(String nombre) {
        if (nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            String nombreSinEspacios = nombre.trim();
            if (!nombreSinEspacios.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private ObservableList<Long> obtenerIdParadas() {
        List<Parada> listaParadas = ps.encontrarTodos();
        ObservableList<Long> listaIdParadas = FXCollections.observableArrayList();

        for (Parada indice : listaParadas) {
            Long idParada = indice.getId();
            listaIdParadas.add(idParada);
        }

        return listaIdParadas;
    }

    private List<Servicio> obtenerListaServicios() {
        return svs.encontrarTodos();
    }

    private Servicio cargarServicio() {
        Servicio servicio = tvServiciosEditar.getSelectionModel().getSelectedItem();

        if (servicio != null) {
            tfNombreServicio.setText(servicio.getNombre());
            tfPrecioServicio.setText(String.valueOf(servicio.getPrecio()));

            ObservableList<Long> idParadas = FXCollections.observableArrayList(servicio.getIdParadas());
            tvParadasUsar.setItems(idParadas);
        }

        return servicio;
    }
}