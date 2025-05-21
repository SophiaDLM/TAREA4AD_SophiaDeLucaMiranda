package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import org.springframework.stereotype.Component;

/***
 * Clase SesionUsuario que permite manejar la sesión de un usuario luego de iniciar sesión y así
 * acceder a sus datos para utilizarlos en los métodos disponibles.
 * Con esta clase se evita el uso de una variable estática que rompería el patrón modelo-vista-controlador.
 */
@Component
public class SesionUsuario {
    private Credenciales credenciales;

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }
}