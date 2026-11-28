package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.InventarioDAO;
import com.ferrered.capa.datos.entidades.Inventario;
import java.sql.Connection;
import java.util.List;

/**
 * Implementación JDBC de InventarioDAO. Responsable: Módulo C - Compras, Inventario y Reportes.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class InventarioDAOImpl implements InventarioDAO {

    @Override
    public Inventario buscarPorProducto(int idProducto) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public void ajustarStock(Connection cn, int idProducto, int cantidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public boolean actualizarStockMinimo(int idProducto, int stockMinimo) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public List<Object[]> listarAlertasStock(String filtroNombre, Integer idCategoria) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }
}
