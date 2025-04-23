package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import java.util.List;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Credenciales;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.CredencialesRepositorio;

/***
 * Clase CredencialesServicio que se encarga de gestionar las transacciones con la base de datos
 * utilizando métodos predefinidos y personalizados que provienen de la interfaz CredencialesRepositorio
 * y que ésta, a su vez, hereda los métodos de JpaRepository.
 *
 * Es importante apuntar que la interfaz contiene métodos anotados con @Query.
 */
@Service
public class CredencialesServicio {
    @Autowired
    private CredencialesRepositorio credencialesRep;

    //Métodos predefinidos:
    public Credenciales guardar(Credenciales entity) {
        return credencialesRep.save(entity);
    }

    public Credenciales actualizar(Credenciales entity) {
        return credencialesRep.save(entity);
    }

    public Credenciales encontrarPorId(Long id) {
        return credencialesRep.findById(id).get();
    }

    public List<Credenciales> encontrarTodos() {
        return credencialesRep.findAll();
    }

    //Métodos personalizados:
    public Credenciales encontrarPorNombreUsuario(String nombre) {
        return credencialesRep.findByNombreUsuario(nombre);
    }

    public TipoUsuario autenticar(String usuario, String contraseña) {
        Credenciales usu = this.encontrarPorNombreUsuario(usuario);

        if(usu == null) {
            return TipoUsuario.INVITADO;
        } else {
            if(contraseña.equals(usu.getContraseña()) && usu.getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
                return TipoUsuario.ADMINISTRADOR;

            } else if (contraseña.equals(usu.getContraseña()) && usu.getTipoUsuario().equals(TipoUsuario.PEREGRINO)) {
                return TipoUsuario.PEREGRINO;

            } else if (contraseña.equals(usu.getContraseña()) && usu.getTipoUsuario().equals(TipoUsuario.PARADA)) {
                return TipoUsuario.PARADA;

            } else {
                return TipoUsuario.INVITADO;
            }
        }
    }
}