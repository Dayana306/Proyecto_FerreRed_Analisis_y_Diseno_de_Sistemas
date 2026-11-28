package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.DevolucionDAO;
import com.ferrered.capa.datos.dao.VentaDAO;
import com.ferrered.capa.datos.dao.impl.DevolucionDAOImpl;
import com.ferrered.capa.datos.dao.impl.VentaDAOImpl;

/**
 * CUS-07 Procesar devolución / anulación (RF-15).
 * Responsable: Módulo B - Ventas y Devoluciones.
 */
@SuppressWarnings("unused")
public class DevolucionController {

    private final DevolucionDAO devolucionDAO = new DevolucionDAOImpl();
    private final VentaDAO ventaDAO = new VentaDAOImpl();

    // TODO: buscarVenta(serie, numero): si estado == 'Anulada' -> 'La venta seleccionada ya se encuentra anulada' (A1)
    // TODO: procesar(): cantidad devuelta <= vendida (A2), confirmar y llamar devolucionDAO.registrarDevolucion()
}
