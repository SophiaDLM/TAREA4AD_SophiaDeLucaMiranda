package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo;

import java.util.Objects;

import jakarta.persistence.*;

/***
 * Clase Credenciales del tipo @Entity que generará una tabla en la base de datos y desde la cuál se
 * podrán manejar los datos gracias al uso de las clases @Service.
 */
@Entity
@Table(name = "Credenciales")
public class Credenciales {
    //Atributos de la clase:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "nombreUsuario", unique = true)
    private String nombreUsuario;

    private String contraseña;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    //Constructores de la clase:
    public Credenciales() {

    }

    public Credenciales(String nombreUsuario, String contraseña, TipoUsuario tipoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
    }

    //Getters y Setters de la clase:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    //Métodos básicos:
    @Override
    public int hashCode() {
        return Objects.hash(contraseña, id, nombreUsuario, tipoUsuario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Credenciales other = (Credenciales) obj;
        return Objects.equals(contraseña, other.contraseña) && Objects.equals(id, other.id)
                && Objects.equals(nombreUsuario, other.nombreUsuario) && tipoUsuario == other.tipoUsuario;
    }
}