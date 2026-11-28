package com.ferrered.capa.datos.dao;

import com.ferrered.capa.datos.entidades.DetalleVenta;
import com.ferrered.capa.datos.entidades.Devolucion;
import java.util.List;

/**
 * DAO de Devolucion. Responsable: Módulo B - Ventas y Devoluciones.
 */
public interface DevolucionDAO {

    /**
     * CUS-07: en UNA transacción registra la devolución, cambia el estado de la venta
     * y reingresa stock con InventarioDAO.ajustarStock(cn, id, +cantidad).
     * @param items detalles devueltos (cantidad = unidades devueltas)
     */
    int registrarDevolucion(Devolucion devolucion, List<DetalleVenta> items, boolean anulacionTotal) throws Exception;

    List<Devolucion> listarPorVenta(int idVenta) throws Exception;
}
