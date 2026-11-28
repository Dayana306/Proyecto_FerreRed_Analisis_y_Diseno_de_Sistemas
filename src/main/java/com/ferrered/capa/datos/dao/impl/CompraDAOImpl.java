package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.CompraDAO;
import com.ferrered.capa.datos.entidades.Compra;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementación JDBC de CompraDAO. Responsable: Módulo C - Compras, Inventario y Reportes.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class CompraDAOImpl implements CompraDAO {

    @Override
    public int registrarCompra(Compra compra) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public List<Compra> listarPorFechas(LocalDate inicio, LocalDate fin) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }
}
