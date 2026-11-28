package com.ferrered.capa.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ferrered.capa.datos.entidades.DetalleVenta;
import com.ferrered.capa.datos.entidades.Venta;
import com.ferrered.capa.negocio.util.NegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/** Pruebas unitarias del cálculo de venta (sección 3.9.3 del informe). */
class VentaControllerTest {

    private DetalleVenta item(String precio, int cant) {
        DetalleVenta d = new DetalleVenta();
        d.setPrecioUnitario(new BigDecimal(precio));
        d.setCantidad(cant);
        return d;
    }

    @Test
    void calculaTotalesConIgvIncluido() {
        Venta v = new Venta();
        v.getDetalles().add(item("11.80", 2));   // 23.60
        v.getDetalles().add(item("5.90", 1));    //  5.90
        new VentaController().calcularTotales(v);
        assertEquals(new BigDecimal("29.50"), v.getTotal());
        assertEquals(new BigDecimal("25.00"), v.getSubtotal());
        assertEquals(new BigDecimal("4.50"), v.getImpuesto());
    }

    @Test
    void rechazaPagoInsuficiente() {
        VentaController c = new VentaController();
        assertThrows(NegocioException.class, () -> c.validarPago(new BigDecimal("50.00"), new BigDecimal("40.00")));
    }

    @Test
    void calculaVuelto() throws Exception {
        assertEquals(new BigDecimal("10.00"),
                new VentaController().validarPago(new BigDecimal("40.00"), new BigDecimal("50.00")));
    }
}
