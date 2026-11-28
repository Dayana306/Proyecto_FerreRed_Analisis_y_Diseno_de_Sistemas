package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.CompraDAO;
import com.ferrered.capa.datos.dao.ProductoDAO;
import com.ferrered.capa.datos.dao.impl.CompraDAOImpl;
import com.ferrered.capa.datos.dao.impl.ProductoDAOImpl;

/**
 * CUS-05 Registrar compra de mercadería (RF-11, RF-12).
 * Responsable: Módulo C - Compras, Inventario y Reportes.
 */
@SuppressWarnings("unused")
public class CompraController {

    private final CompraDAO compraDAO = new CompraDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    // TODO: agregarItem(producto, cantidad, costoUnitario), quitarItem(), calcularTotal()
    // TODO: confirmar(compra) -> compraDAO.registrarCompra(); cancelar() descarta sin tocar inventario (A1)
}
