package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista.VistaFxml;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/***
 * Clase ManejadorEscenas que se encarga de cargar una escena específica, prepararla, cambiarla, etc.
 */
public class ManejadorEscenas {
    private final Stage escenaPrincipal;
    private final CargadorSpringFXML cSpringFxml;

    public ManejadorEscenas(CargadorSpringFXML cSpringFxml, Stage escena) {
        this.cSpringFxml = cSpringFxml;
        this.escenaPrincipal = escena;
    }

    public void cambiarEscena(final VistaFxml vista) {
        Parent vistaJerarquiaNodoRaiz = cargarVistaJerarquiaNodo(vista.getArchivoFxml());
        mostrar(vistaJerarquiaNodoRaiz, vista.getTitulo());
    }

    private Parent cargarVistaJerarquiaNodo(String rutaArchivoFxml) {
        Parent nodoRaiz = null;

        try {
            nodoRaiz = cSpringFxml.cargar(rutaArchivoFxml);
            Objects.requireNonNull(nodoRaiz, "Un nodo raíz FXML no puede ser null");
        } catch (Exception e) {
            System.out.println("Error al cargar la vista "+rutaArchivoFxml+" - "+e.getMessage());
        }

        return nodoRaiz;
    }

    private void mostrar(final Parent nodoRaiz, String titulo) {
        Scene escena = prepararEscena(nodoRaiz);

        escenaPrincipal.setTitle(titulo);
        escenaPrincipal.setScene(escena);
        escenaPrincipal.centerOnScreen();
        escenaPrincipal.setResizable(false);

        try {
            escenaPrincipal.show();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error con la escena "+titulo+" - "+e.getMessage());
        }
    }

    private Scene prepararEscena(Parent nodoRaiz) {
        Scene escena = escenaPrincipal.getScene();

        if(escena == null) {
            escena = new Scene(nodoRaiz);
        }
        escena.setRoot(nodoRaiz);

        return escena;
    }
}