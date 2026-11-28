package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.CategoriaDAO;
import com.ferrered.capa.datos.entidades.Categoria;
import java.util.List;

/**
 * Implementación JDBC de CategoriaDAO. Responsable: Módulo A - Seguridad y Catálogo.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public int insertar(Categoria entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public boolean actualizar(Categoria entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public boolean desactivar(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public Categoria buscarPorId(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public List<Categoria> listar() throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }
}
