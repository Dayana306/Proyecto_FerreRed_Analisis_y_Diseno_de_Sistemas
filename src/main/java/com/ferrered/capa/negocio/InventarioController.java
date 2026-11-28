package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.InventarioDAO;
import com.ferrered.capa.datos.dao.impl.InventarioDAOImpl;

/**
 * CUS-03 Consultar alertas de stock mínimo (RF-03, RF-04).
 * Responsable: Módulo C - Compras, Inventario y Reportes.
 */
@SuppressWarnings("unused")
public class InventarioController {

    private final InventarioDAO inventarioDAO = new InventarioDAOImpl();

    // TODO: listarAlertas(filtro, idCategoria) -> si vacío mostrar 'Todos los productos cuentan con niveles de stock óptimos' (A1)
    // TODO: verificarDisponibilidad(idProducto, cantidad) -> usado por VentaController (Módulo B)
    // TODO: actualizarStockMinimo()
}
