package com.ferrered.capa.datos.dao;

import com.ferrered.capa.datos.entidades.Inventario;
import java.sql.Connection;
import java.util.List;

/**
 * DAO de Inventario. Responsable: Módulo C - Compras, Inventario y Reportes.
 *
 * CONTRATO COMPARTIDO: los módulos de Ventas (B), Compras (C) y Devoluciones (B)
 * llaman a {@link #ajustarStock} pasando SU conexión, para que el movimiento de
 * stock quede dentro de la misma transacción que la venta/compra/devolución.
 */
public interface InventarioDAO {

    Inventario buscarPorProducto(int idProducto) throws Exception;

    /**
     * Suma (cantidad positiva) o resta (cantidad negativa) unidades del stock.
     * Debe fallar si el stock resultante quedara negativo.
     */
    void ajustarStock(Connection cn, int idProducto, int cantidad) throws Exception;

    boolean actualizarStockMinimo(int idProducto, int stockMinimo) throws Exception;

    /** CUS-03: productos con stock_actual <= stock_minimo (vista_stock_minimo). */
    List<Object[]> listarAlertasStock(String filtroNombre, Integer idCategoria) throws Exception;
}
