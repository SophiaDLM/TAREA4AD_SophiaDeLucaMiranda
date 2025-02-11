package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista;

import java.util.ResourceBundle;

/***
 * Clase Enum VistaFxml que sirve para mostrar las diferentes ventanas
 * en función del tipo de usuario, como administrador, peregrino o parada y,
 * siendo la pantalla de INICIARSESION para usuarios no registrados o invitados.
 */
public enum VistaFxml {
    INICIARSESION {
        @Override
        public String getTitulo() {
            return getStringDelResourceBundle("iniciarsesion.titulo");
        }

        @Override
        public String getArchivoFxml() {
            return "/fxml/IniciarSesion.fxml";
        }
    },

    ADMINISTRADOR {
        @Override
        public String getTitulo() {
            return getStringDelResourceBundle("administrador.titulo");
        }

        @Override
        public String getArchivoFxml() {
            return "/fxml/Administrador.fxml";
        }
    },

    PEREGRINO {
        @Override
        public String getTitulo() {
            return getStringDelResourceBundle("peregrino.titulo");
        }

        @Override
        public String getArchivoFxml() {
            return "/fxml/Peregrino.fxml";
        }
    },

    PARADA {
        @Override
        public String getTitulo() {
            return getStringDelResourceBundle("parada.titulo");
        }

        @Override
        public String getArchivoFxml() {
            return "/fxml/Parada.fxml";
        }
    };

    /***
     * Método abstracto que sirve para obtener el string con el nombre de la ventana.
     * Se utiliza junto con el método .getStringDelResourceBundle.
     *
     * @return string con el título de la ventana.
     */
    public abstract String getTitulo();

    /***
     * Método abstracto que sirve para obtener el string con la ruta de la ventana correspondiente.
     *
     * @return string con la ruta del archivo FXML.
     */
    public abstract String getArchivoFxml();

    /***
     * Método que sirve para obtener el string del título del archivo bundle.properties.
     *
     * @param clave con el nombre + .título como por ejemplo: "iniciarsesion.titulo".
     * @return string con el título de la ventana.
     */
    public String getStringDelResourceBundle(String clave) {
        return ResourceBundle.getBundle("bundle").getString(clave);
    }
}