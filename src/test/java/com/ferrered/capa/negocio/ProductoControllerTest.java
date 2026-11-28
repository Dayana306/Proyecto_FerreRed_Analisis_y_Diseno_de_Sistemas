package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.entidades.Producto;
import com.ferrered.capa.negocio.util.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de las validaciones de CUS-02 (flujo A2).
 * No necesitan base de datos: validar() no usa los DAO.
 */
class ProductoControllerTest {

    private ProductoController controller;
    private Producto p;

    @BeforeEach
    void preparar() {
        controller = new ProductoController(null, null);
        p = new Producto();
        p.setCodigoBarras("7750000000011");
        p.setNombre("Martillo de uña 16 oz");
        p.setIdCategoria(1);
        p.setPrecioCompra(new BigDecimal("18.00"));
        p.setPrecioVenta(new BigDecimal("27.00"));
        p.setStockMinimo(5);
    }

    @Test
    void productoValidoPasa() {
        assertDoesNotThrow(() -> controller.validar(p));
    }

    @Test
    void codigoVacioFalla() {
        p.setCodigoBarras("  ");
        NegocioException ex = assertThrows(NegocioException.class, () -> controller.validar(p));
        assertEquals("Ingrese el código de barras", ex.getMessage());
    }

    @Test
    void sinCategoriaFalla() {
        p.setIdCategoria(0);
        assertThrows(NegocioException.class, () -> controller.validar(p));
    }

    @Test
    void precioNegativoFalla() {
        p.setPrecioCompra(new BigDecimal("-1"));
        assertThrows(NegocioException.class, () -> controller.validar(p));
    }

    @Test
    void precioVentaMenorQueCompraFalla() {
        p.setPrecioVenta(new BigDecimal("10.00"));
        assertThrows(NegocioException.class, () -> controller.validar(p));
    }

    @Test
    void stockMinimoNegativoFalla() {
        p.setStockMinimo(-3);
        assertThrows(NegocioException.class, () -> controller.validar(p));
    }
}
