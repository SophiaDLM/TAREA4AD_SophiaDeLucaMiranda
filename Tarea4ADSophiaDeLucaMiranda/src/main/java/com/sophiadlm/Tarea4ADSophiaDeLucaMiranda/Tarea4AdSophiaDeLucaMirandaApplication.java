package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.config.ManejadorEscenas;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.vista.VistaFxml;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

//AÑADIR MÉTODOS DE VOLVER Y CERRAR SESIÓN EN TODO PARA FACILITAR LA NAVEGACIÓN ENTRE PANTALLAS

/***
 * Clase Tarea4AdSophiaDeLucaMirandaApplication que contiene los métodos necesarios para
 * arrancar la aplicación.
 */
@SpringBootApplication
public class Tarea4AdSophiaDeLucaMirandaApplication extends Application {
	protected ConfigurableApplicationContext contextoAplicacion;
	protected ManejadorEscenas me;

	@Override
	public void init() throws Exception {
		contextoAplicacion = contextoAplicacionSpringBoot();
	}

	public static void main(final String[] args) {
		Application.launch(args);
		System.out.println("\nFUNCIONANDO\n");
	}

	@Override
	public void start(Stage escenaPrincipal) throws IOException {
		me = contextoAplicacion.getBean(ManejadorEscenas.class, escenaPrincipal);
		mostrarEscenaPrincipal();
	}

	protected void mostrarEscenaPrincipal() {
		me.cambiarEscena(VistaFxml.INICIARSESION);
	}

	private ConfigurableApplicationContext contextoAplicacionSpringBoot() {
		SpringApplicationBuilder constructor = new SpringApplicationBuilder(Tarea4AdSophiaDeLucaMirandaApplication.class);
		String[] args = getParameters().getRaw().stream().toArray(String[]::new);
		return constructor.run(args);
	}
}