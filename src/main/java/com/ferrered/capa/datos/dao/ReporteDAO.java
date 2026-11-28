package com.ferrered.capa.datos.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DAO de reportes (consultas sobre vista_reporte_ventas). Responsable: Módulo C - Compras, Inventario y Reportes.
 */
public interface ReporteDAO {

    /** CUS-06: filas de ventas en el rango, con filtros opcionales (null = sin filtro). */
    List<Object[]> ventasPorRango(LocalDate inicio, LocalDate fin, Integer idUsuario,
                                  Integer idMetodoPago, Integer idCategoria) throws Exception;

    /** Totales: cantidadVentas, subtotal, impuesto, total, gananciaEstimada. */
    Map<String, Object> resumen(LocalDate inicio, LocalDate fin) throws Exception;

    List<Object[]> productosMasVendidos(LocalDate inicio, LocalDate fin, int limite) throws Exception;
}
