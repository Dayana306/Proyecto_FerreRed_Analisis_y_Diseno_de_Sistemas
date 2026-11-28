package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.ClienteDAO;
import com.ferrered.capa.datos.entidades.Cliente;
import java.util.List;

/**
 * Implementación JDBC de ClienteDAO. Responsable: Módulo B - Ventas y Devoluciones.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public int insertar(Cliente entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public boolean actualizar(Cliente entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public boolean desactivar(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public Cliente buscarPorId(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public List<Cliente> listar() throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public Cliente buscarPorDocumento(String dniRuc) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }
}
