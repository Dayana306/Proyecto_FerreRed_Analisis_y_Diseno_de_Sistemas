package com.ferrered.capa.datos.dao;

import java.util.List;

/**
 * Contrato genérico CRUD que extienden los DAO de tablas maestras.
 * @param <T> entidad
 */
public interface CrudDAO<T> {

    int insertar(T entidad) throws Exception;

    boolean actualizar(T entidad) throws Exception;

    /** Eliminación lógica: cambia estado = false. */
    boolean desactivar(int id) throws Exception;

    T buscarPorId(int id) throws Exception;

    List<T> listar() throws Exception;
}
