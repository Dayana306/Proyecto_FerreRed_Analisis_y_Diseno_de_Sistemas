package com.ferrered.capa.datos.dao;

import com.ferrered.capa.datos.entidades.Cliente;

/**
 * DAO de Cliente. Responsable: Módulo B - Ventas y Devoluciones.
 */
public interface ClienteDAO extends CrudDAO<Cliente> {

    Cliente buscarPorDocumento(String dniRuc) throws Exception;
}
