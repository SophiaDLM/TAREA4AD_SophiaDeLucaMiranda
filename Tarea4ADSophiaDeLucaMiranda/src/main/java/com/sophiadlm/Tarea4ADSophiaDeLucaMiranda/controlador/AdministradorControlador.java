package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.controlador;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config.ManejadorEscenas;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Credenciales;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Parada;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Servicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.TipoUsuario;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.CredencialesServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.ParadaServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios.ServicioServicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista.VistaFxml;
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

/***
 * Clase AdministradorControlador que se encarga de manejar las acciones
 * disponibles de un administrador como lo es acceder a la ayuda de usuario
 * (no implementada aún), al método para añadir una nueva parada a la base de datos
 * y a cerrar sesión si así lo desea.
 *
 * Esta clase implementa Initializable para el uso de JavaFX.
 */
@Controller
public class AdministradorControlador implements Initializable {
    //Elementos relacionados con el archivo FXML:
    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfRegion;

    @FXML
    private TextField tfUsuario;

    @FXML
    private PasswordField pfContraseña;

    @FXML
    private TextField tfNomServicio;

    @FXML
    private TextField tfPrecioServicio;

    @FXML
    private ListView<Long> lstConParServicio;

    @FXML
    private ListView<Servicio> lstEditarServicio;

    @FXML
    private GridPane pnlNuevaParada;

    @FXML
    private GridPane pnlNuevoServicio;

    @FXML
    private GridPane pnlEditarServicio;

    //Elementos relacionados con el manejo de las escenas:
    @Lazy
    @Autowired
    private ManejadorEscenas me;

    //Elementos relacionados con la manipulación de la base de datos:
    @Autowired
    private CredencialesServicio cs;

    @Autowired
    private ParadaServicio ps;

    private ServicioServicio svs = new ServicioServicio();

    @FXML
    private void cambiarPanelNuevaParada() {
        pnlNuevaParada.setVisible(true);
        pnlNuevoServicio.setVisible(false);
        pnlEditarServicio.setVisible(false);
    }

    @FXML
    private void cambiarPanelNuevoServicio() {
        pnlNuevaParada.setVisible(false);
        pnlNuevoServicio.setVisible(true);
        pnlEditarServicio.setVisible(false);
    }

    @FXML
    private void cambiarPanelEditarServicio() {
        pnlNuevaParada.setVisible(false);
        pnlNuevoServicio.setVisible(false);
        pnlEditarServicio.setVisible(true);
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
     * Método cerrarSesion que lanza una alerta pidiendo al usuario que
     * confirme su decisión. Si el usuario pulsa en el botón de aceptar, se
     * camibia la ventana a la de INICIARSESION.
     *
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

    /***
     * Método nuevaParada que obtiene los datos de los campos en la interfaz gráfica, los valida y posteriormente
     * los registra en la base de datos, lanzándo una alerta si todo ha salido bien.
     */
    @FXML
    public void nuevaParada() {
        try {
            String nombre = tfNombre.getText();
            char region = tfRegion.getText().charAt(0);
            String usuario = tfUsuario.getText();
            String contraseña = pfContraseña.getText();

            if (validarNombre(nombre)) {
                if (Character.isLetter(region)) {
                    if (usuario.matches("[a-zA-Z0-9_]+")) {
                        if (contraseña.matches("[a-zA-Z0-9_]{8}")) {
                            Credenciales nuevasCredenciales = new Credenciales(usuario, contraseña, TipoUsuario.PARADA);
                            nuevasCredenciales = cs.guardar(nuevasCredenciales);

                            Parada nuevaParada = new Parada(nuevasCredenciales.getId(), nombre, region, usuario);
                            nuevaParada = ps.guardar(nuevaParada);

                            Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
                            confirmacion.setTitle("Operación Exitosa");
                            confirmacion.setHeaderText("Se ha registrado el usuario y la parada exitosamente");
                            confirmacion.showAndWait();

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

    // MÉTODO NUEVO/EDITAR SERVICIO - DB4O
    public void nuevoServicio() {
        try {
            String nomServicio = tfNomServicio.getText();
            String precioServicio = tfPrecioServicio.getText();
            List<Long> listaIdParadas = lstConParServicio.getSelectionModel().getSelectedItems();

            if(validarNombre(nomServicio)) {
                if(svs.encontrarPorNombre(nomServicio) == null) {
                    if(precioServicio.matches("^(\\d{1,3}(\\.\\d{3})*(\\,\\d{2})?)?€?$")) {



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
                    error.setHeaderText("Servicio existente");
                    error.setContentText("No se ha podido añadir el nuevo servicio porque ya existe");
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
            e.printStackTrace();
        }
    }

    public void editarServicio() {


        //Se da clic en el boton de editar servicio y se muestra una lista con los servicios
        //De esa lista se selecciona un servicio y se da editar
        //Se cambia el panel
        //Se obtiene el nombre (sin números, caracteres especiales o en blanco)
        //Se obtiene el precio (sin letras)
        //Se obtiene el conjunto de paradas (Lista de ID)
        //Se da clic en guardar
        //Se borran los datos en la pantalla

        //Se hace un update
    }


    /***
     * Método initialize que no ha sido utilizado porque en este caso no es
     * necesario, pero es obligatorio implementarlo. En casos en los que se necesite
     * cargar algún dato en la pantalla apenas arranque la aplicación, sería
     * dentro de este método que se colocaría.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstConParServicio.getItems().addAll(obtenerIdParadas());
//        lstEditarServicio.getItems().addAll(obtenerListaServicios());
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

    private List<Long> obtenerIdParadas() {
        List<Parada> listaParadas = ps.encontrarTodos();
        List<Long> listaIdParadas = new ArrayList<>();

        for(Parada indice: listaParadas) {
            Long idParada = indice.getId();
            listaIdParadas.add(idParada);
        }

        return listaIdParadas;
    }

    private List<Servicio> obtenerListaServicios() {
        return svs.encontrarTodos();
    }

//    private void actualizarParadas(List<Long> idParadas, Long idServicio) {
//        for(Long indice: idParadas) {
//            Parada parada = ps.encontrarPorId(indice);
//
//            if(parada != null) {
//                parada.setIdServicios(idServicio.toString());
//                ps.actualizar(parada);
//            }
//        }
//    }
}