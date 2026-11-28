package com.ferrered.capa.presentacion;

/**
 * CUS-03: tabla de productos con stock_actual <= stock_minimo, filtro por categoría/nombre, botón Generar Solicitud de Compra.
 * Responsable: Módulo C - Compras, Inventario y Reportes.
 * Debe comunicarse SOLO con su controlador de capa.negocio (nunca con DAO directamente).
 */
public class FrmInventario extends FrmBase {

    public FrmInventario() {
        super("Alertas de Stock Mínimo");
        // TODO Módulo C - Compras, Inventario y Reportes: reemplazar el placeholder por los componentes reales
        enConstruccion("Módulo C - Compras, Inventario y Reportes", "CUS-03: tabla de productos con stock_actual <= stock_minimo, filtro por categoría/nombre, botón Generar Solicitud de Compra.");
    }
}
