package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.ProductoDAO;
import com.ferrered.capa.datos.entidades.Producto;
import java.util.List;

/**
 * Implementación JDBC de ProductoDAO. Responsable: Módulo A - Seguridad y Catálogo.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public int insertar(Producto entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public boolean actualizar(Producto entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public boolean desactivar(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public Producto buscarPorId(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public List<Producto> listar() throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public Producto buscarPorCodigo(String codigoBarras) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public List<Producto> buscar(String texto, Integer idCategoria) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public int insertarConInventario(Producto p, int stockInicial, int stockMinimo) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }
}
