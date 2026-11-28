package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.dao.VentaDAO;
import com.ferrered.capa.datos.entidades.Comprobante;
import com.ferrered.capa.datos.entidades.MetodoPago;
import com.ferrered.capa.datos.entidades.Venta;
import java.util.List;

/**
 * Implementación JDBC de VentaDAO. Responsable: Módulo B - Ventas y Devoluciones.
 * Tomar como ejemplo UsuarioDAOImpl (try-with-resources + PreparedStatement).
 * Para operaciones con cabecera/detalle: cn.setAutoCommit(false) ... cn.commit() / cn.rollback().
 */
@SuppressWarnings("unused")
public class VentaDAOImpl implements VentaDAO {

    @Override
    public int registrarVenta(Venta venta, Comprobante comprobante) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public Venta buscarPorId(int idVenta) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public Venta buscarPorComprobante(String serie, String numero) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public String siguienteNumeroComprobante(String serie) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public List<MetodoPago> listarMetodosPago() throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }

    @Override
    public boolean cambiarEstado(int idVenta, String estado) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo B - Ventas y Devoluciones");
    }
}
