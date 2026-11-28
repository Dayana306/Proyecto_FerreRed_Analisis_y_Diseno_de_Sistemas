package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.ReporteDAO;
import com.ferrered.capa.datos.dao.impl.ReporteDAOImpl;

/**
 * CUS-06 Generar reporte de ventas (RF-14).
 * Responsable: Módulo C - Compras, Inventario y Reportes.
 */
@SuppressWarnings("unused")
public class ReporteController {

    private final ReporteDAO reporteDAO = new ReporteDAOImpl();

    // TODO: generar(inicio, fin, filtros): validar inicio <= fin (A2), sin datos -> aviso (A1)
    // TODO: exportarCSV/Excel y exportarPDF (sugerencia: CSV abre en Excel sin librerías extra)
}
