package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.ResourceBundle;

/***
 * Clase AppJavaConfig que se encarga de configurar la aplicación definiendo
 * los beans necesarios para la gestión de escenas en JavaFX con Spring.
 */
@Configuration
public class AppJavaConfig {
    @Autowired
    CargadorSpringFXML cSpringFXML;

    /***
     * Bean ResourceBundle que retorna el bundle con el nombre que indica.
     * @return bundle.
     */
    @Bean
    public ResourceBundle rb() {
        return ResourceBundle.getBundle("bundle");
    }

    /***
     * Bean ManejadorEscenas que maneja las escenas de la aplicación.
     *
     * @param escena principal.
     * @return nuevo objeto ManejadorEscenas.
     * @throws IOException si no se puede encontrar y/o leer el archivo necesario.
     */
    @Bean
    @Lazy(value = true)
    public ManejadorEscenas me(Stage escena) throws IOException {
        return new ManejadorEscenas(cSpringFXML, escena);
    }
}