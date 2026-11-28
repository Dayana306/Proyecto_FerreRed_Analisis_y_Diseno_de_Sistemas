package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.ProveedorDAO;
import com.ferrered.capa.datos.entidades.Proveedor;
import java.util.List;

/**
 * Implementación JDBC de ProveedorDAO. Responsable: Módulo C - Compras, Inventario y Reportes.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class ProveedorDAOImpl implements ProveedorDAO {

    @Override
    public int insertar(Proveedor entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public boolean actualizar(Proveedor entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public boolean desactivar(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public Proveedor buscarPorId(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public List<Proveedor> listar() throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }
}
