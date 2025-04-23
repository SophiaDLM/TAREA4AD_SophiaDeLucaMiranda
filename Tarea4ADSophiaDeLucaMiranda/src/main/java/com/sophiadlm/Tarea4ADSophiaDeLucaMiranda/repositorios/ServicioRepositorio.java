package com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.repositorios;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.data.DataConexion;
import com.sophiadlm.Tarea4ADSophiaDeLucaMiranda.modelo.Servicio;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServicioRepositorio {
    private ObjectContainer baseDatos;

    public ServicioRepositorio() {
        this.baseDatos = DataConexion.obtenerInstancia();
    }

    public boolean guardarDB4O(Servicio entity) {
        try {
            baseDatos.store(entity);
            baseDatos.commit();
            return true;

        } catch (Exception e) {
            System.out.println(">> FATAL ERROR - No se ha podido guardar el servicio: ");
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarDB4O(Servicio entity) {
        try {
            Servicio servicio = encontrarPorIdDB4O(entity.getId());

            if (servicio != null) {
                servicio.setNombre(entity.getNombre());
                servicio.setPrecio(entity.getPrecio());
                servicio.setIdParadas(entity.getIdParadas());
                baseDatos.store(servicio);
                baseDatos.commit();
                return true;
            }
        } catch (Exception e) {
            System.out.println(">> FATAL ERROR - No se ha podido guardar el servicio: ");
            e.printStackTrace();
        }
        return false;
    }

    public Servicio encontrarPorIdDB4O(Long id) {
        ObjectSet<Servicio> servicios = baseDatos.queryByExample(new Servicio(id, null, 0));
        Servicio servicioEncontrado;

        if(servicios.hasNext()) {
            return servicioEncontrado = servicios.next();
        } else {
            return null;
        }
    }

    public List<Servicio> encontrarTodosDB4O() {
        List<Servicio> listaServicios = baseDatos.query(Servicio.class);
        return listaServicios;
    }

    public Servicio encontrarPorNombreDB4O(String nombre) {
        ObjectSet<Servicio> servicios = baseDatos.queryByExample(new Servicio(null, nombre, 0));
        Servicio servicioEncontrado;

        if(servicios.hasNext()) {
            return servicioEncontrado = servicios.next();
        } else {
            return null;
        }
    }
}