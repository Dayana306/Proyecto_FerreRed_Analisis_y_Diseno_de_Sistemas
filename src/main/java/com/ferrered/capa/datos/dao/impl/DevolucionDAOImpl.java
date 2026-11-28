package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.DevolucionDAO;
import com.ferrered.capa.datos.entidades.DetalleVenta;
import com.ferrered.capa.datos.entidades.Devolucion;
import java.util.List;

/**
 * Implementación JDBC de DevolucionDAO. Responsable: Módulo B - Ventas y Devoluciones.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class DevolucionDAOImpl implements DevolucionDAO {

    @Override
    public int registrarDevolucion(Devolucion devolucion, List<DetalleVenta> items, boolean anulacionTotal) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public List<Devolucion> listarPorVenta(int idVenta) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }
}
