package com.ferrered.capa.datos.dao;

import com.ferrered.capa.datos.entidades.Compra;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO de Compra. Responsable: Módulo C - Compras, Inventario y Reportes.
 */
public interface CompraDAO {

    /**
     * CUS-05: en UNA transacción inserta compra + detalles, actualiza precio_compra
     * del producto e incrementa stock con InventarioDAO.ajustarStock(cn, id, +cantidad).
     */
    int registrarCompra(Compra compra) throws Exception;

    List<Compra> listarPorFechas(LocalDate inicio, LocalDate fin) throws Exception;
}
