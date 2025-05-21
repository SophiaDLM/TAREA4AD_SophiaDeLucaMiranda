package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ResourceBundle;

/***
 * Clase CargadorSpringFXML que se encarga de cargar las vistas de la aplicación.
 */
@Component
public class CargadorSpringFXML {
    private final ResourceBundle rb;
    private final ApplicationContext ac;

    /***
     * Constructor CargadorSpringFXML que inyecta el ResourceBundle y ApplicationContext a la aplicación.
     */
    @Autowired
    public CargadorSpringFXML(ResourceBundle rb, ApplicationContext ac) {
        this.rb = rb;
        this.ac = ac;
    }

    /***
     * Método cargar que carga una escena específica, asignándole un controlador, los recursos necesarios para que
     * funcione y el archivo de la vista.
     *
     * @param rutaFxml que será un string con la ruta en donde se almacena la vista a cargar.
     * @return un objeto del tipo FXMLLoader.
     * @throws IOException si no se puede encontrar y/o leer un archivo.
     */
    public Parent cargar(String rutaFxml) throws IOException {
        FXMLLoader cargador = new FXMLLoader();

        cargador.setControllerFactory(ac::getBean);
        cargador.setResources(rb);
        cargador.setLocation(getClass().getResource(rutaFxml));

        return cargador.load();
    }
}