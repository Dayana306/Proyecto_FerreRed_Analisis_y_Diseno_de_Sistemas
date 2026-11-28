package com.ferrered.capa.datos.dao;

import com.ferrered.capa.datos.entidades.Comprobante;
import com.ferrered.capa.datos.entidades.MetodoPago;
import com.ferrered.capa.datos.entidades.Venta;
import java.util.List;

/**
 * DAO de Venta. Responsable: Módulo B - Ventas y Devoluciones.
 */
public interface VentaDAO {

    /**
     * CUS-04: en UNA transacción inserta venta + detalles + comprobante y descuenta
     * stock usando InventarioDAO.ajustarStock(cn, idProducto, -cantidad).
     * @return id de la venta generada
     */
    int registrarVenta(Venta venta, Comprobante comprobante) throws Exception;

    Venta buscarPorId(int idVenta) throws Exception;

    Venta buscarPorComprobante(String serie, String numero) throws Exception;

    String siguienteNumeroComprobante(String serie) throws Exception;

    List<MetodoPago> listarMetodosPago() throws Exception;

    boolean cambiarEstado(int idVenta, String estado) throws Exception;
}
