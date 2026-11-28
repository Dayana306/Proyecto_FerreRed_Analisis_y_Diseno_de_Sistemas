package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.ProductoDAO;
import com.ferrered.capa.datos.dao.VentaDAO;
import com.ferrered.capa.datos.dao.impl.ProductoDAOImpl;
import com.ferrered.capa.datos.dao.impl.VentaDAOImpl;
import com.ferrered.capa.datos.entidades.DetalleVenta;
import com.ferrered.capa.datos.entidades.Venta;
import com.ferrered.capa.negocio.util.NegocioException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * CUS-04 Registrar venta (RF-05 a RF-09).
 * Responsable: Módulo B - Ventas y Devoluciones.
 * calcularTotales() y validarPago() ya están implementados y tienen prueba JUnit.
 */
@SuppressWarnings("unused")
public class VentaController {

    /** IGV Perú. */
    public static final BigDecimal TASA_IGV = new BigDecimal("0.18");

    /**
     * Supuesto: el precio_venta del producto YA INCLUYE IGV (lo usual en retail).
     * Confirmar con el dueño de FerreRed; si no lo incluye, cambiar a false.
     */
    public static final boolean PRECIO_INCLUYE_IGV = true;

    private final VentaDAO ventaDAO = new VentaDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    /** Recalcula subtotal de cada línea y subtotal/impuesto/total de la venta. */
    public void calcularTotales(Venta venta) {
        BigDecimal suma = BigDecimal.ZERO;
        for (DetalleVenta d : venta.getDetalles()) {
            BigDecimal linea = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);
            d.setSubtotal(linea);
            suma = suma.add(linea);
        }
        BigDecimal subtotal, igv, total;
        if (PRECIO_INCLUYE_IGV) {
            total = suma;
            subtotal = total.divide(BigDecimal.ONE.add(TASA_IGV), 2, RoundingMode.HALF_UP);
            igv = total.subtract(subtotal);
        } else {
            subtotal = suma;
            igv = subtotal.multiply(TASA_IGV).setScale(2, RoundingMode.HALF_UP);
            total = subtotal.add(igv);
        }
        venta.setSubtotal(subtotal);
        venta.setImpuesto(igv);
        venta.setTotal(total);
    }

    /** CUS-04 A2: el monto recibido en efectivo no puede ser menor al total. Devuelve el vuelto. */
    public BigDecimal validarPago(BigDecimal total, BigDecimal montoRecibido) throws NegocioException {
        if (montoRecibido == null || montoRecibido.compareTo(total) < 0) {
            BigDecimal falta = total.subtract(montoRecibido == null ? BigDecimal.ZERO : montoRecibido);
            throw new NegocioException("Pago insuficiente. Saldo pendiente: S/ " + falta);
        }
        return montoRecibido.subtract(total);
    }

    // TODO: agregarProducto(venta, codigoBarras, cantidad) -> buscar por código, verificar stock (A1 'Stock insuficiente. Disponible: N')
    // TODO: quitarProducto(), confirmarVenta(venta, tipoComprobante) -> ventaDAO.registrarVenta()
}
