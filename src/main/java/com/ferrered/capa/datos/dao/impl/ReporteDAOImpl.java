package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.ReporteDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Implementación JDBC de ReporteDAO. Responsable: Módulo C - Compras, Inventario y Reportes.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class ReporteDAOImpl implements ReporteDAO {

    @Override
    public List<Object[]> ventasPorRango(LocalDate inicio, LocalDate fin, Integer idUsuario, Integer idMetodoPago, Integer idCategoria) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public Map<String, Object> resumen(LocalDate inicio, LocalDate fin) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }

    @Override
    public List<Object[]> productosMasVendidos(LocalDate inicio, LocalDate fin, int limite) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo C - Compras, Inventario y Reportes");
    }
}
