package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.servicios;

import com.db4o.ObjectContainer;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexion;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.ConjuntoContratado;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Servicio;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios.ServicioRepositorio;

import java.util.List;

//MODIFICAR Y DOCUMENTAR
public class ServicioServicio implements ServicioRepositorio {
    private ObjectContainer baseDatos = DataConexion.obtenerInstancia();

    @Override
    public Servicio guardar(Servicio entity) {
        baseDatos.store(entity);
        baseDatos.commit();
        return entity;
    }

    @Override
    public Servicio actualizar(Servicio entity) {
        Servicio servicio = encontrarPorId(entity.getId());

        if(servicio != null) {
            servicio.setNombre(entity.getNombre());
            servicio.setPrecio(entity.getPrecio());
            baseDatos.store(entity);
            baseDatos.commit();
            return servicio;
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Servicio entity) {
        baseDatos.delete(entity);
        baseDatos.commit();
    }

    @Override
    public void borrarPorId(Long id) {
        Servicio servicio = encontrarPorId(id);

        if(servicio != null) {
            baseDatos.delete(servicio);
            baseDatos.commit();
        }
    }

    @Override
    public void borrarPorLote(List<Servicio> servicios) {
        for(Servicio indice: servicios) {
            baseDatos.delete(indice);
        }
        baseDatos.commit();
    }

    @Override
    public Servicio encontrarPorId(Long id) {
        List<Servicio> servicios = baseDatos.query(Servicio.class);

        for(Servicio indice: servicios) {
            if(indice.getId().equals(id)) {
                return indice;
            }
        }
        return null;
    }

    @Override
    public List<Servicio> encontrarTodos() {
        List<Servicio> listaServicios = baseDatos.query(Servicio.class);
        return listaServicios;
    }

    @Override
    public Servicio encontrarPorNombre(String nombre) {
        List<Servicio> servicios = baseDatos.query(Servicio.class);

        for(Servicio indice: servicios) {
            if(indice.getNombre().equals(nombre)) {
                return indice;
            }
        }
        return null;
    }
}