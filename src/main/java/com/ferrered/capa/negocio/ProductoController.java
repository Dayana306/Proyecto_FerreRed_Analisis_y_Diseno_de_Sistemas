package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.CategoriaDAO;
import com.ferrered.capa.datos.dao.ProductoDAO;
import com.ferrered.capa.datos.dao.impl.CategoriaDAOImpl;
import com.ferrered.capa.datos.dao.impl.ProductoDAOImpl;
import com.ferrered.capa.datos.entidades.Categoria;
import com.ferrered.capa.datos.entidades.Producto;
import com.ferrered.capa.negocio.util.NegocioException;
import com.ferrered.capa.negocio.util.Validador;
import java.math.BigDecimal;
import java.util.List;

/**
 * CUS-02 Gestionar Productos (RF-01, RF-02) + categorías.
 * Responsable: Módulo A - Seguridad y Catálogo.
 *
 * Contiene TODAS las reglas de negocio del producto. La vista (FrmProductos) solo
 * arma el objeto y muestra mensajes; el DAO solo ejecuta SQL.
 */
public class ProductoController {

    private final ProductoDAO productoDAO;
    private final CategoriaDAO categoriaDAO;

    public ProductoController() {
        this(new ProductoDAOImpl(), new CategoriaDAOImpl());
    }

    /** Constructor para pruebas unitarias (permite inyectar DAOs falsos). */
    public ProductoController(ProductoDAO productoDAO, CategoriaDAO categoriaDAO) {
        this.productoDAO = productoDAO;
        this.categoriaDAO = categoriaDAO;
    }

    // ------------------------------------------------------------------ consultas

    public List<Categoria> listarCategorias() throws Exception {
        return categoriaDAO.listar();
    }

    public List<Producto> listar() throws Exception {
        return productoDAO.listar();
    }

    /** RF-02: búsqueda por nombre/código y categoría (idCategoria 0 o null = todas). */
    public List<Producto> buscar(String texto, Integer idCategoria) throws Exception {
        return productoDAO.buscar(texto, idCategoria);
    }

    /** Usado también por los módulos B (ventas) y C (compras). */
    public Producto buscarPorCodigo(String codigo) throws Exception {
        if (Validador.vacio(codigo)) {
            return null;
        }
        return productoDAO.buscarPorCodigo(codigo);
    }

    // ------------------------------------------------------------------ operaciones

    /** CUS-02 flujo normal: registra producto + inventario inicial. */
    public int registrar(Producto p) throws Exception {
        validar(p);
        if (p.getStockActual() < 0) {
            throw new NegocioException("El stock inicial no puede ser negativo");
        }
        verificarCodigoUnico(p.getCodigoBarras(), 0);
        return productoDAO.insertarConInventario(p, p.getStockActual(), p.getStockMinimo());
    }

    public boolean modificar(Producto p) throws Exception {
        if (p.getIdProducto() <= 0) {
            throw new NegocioException("Seleccione un producto de la lista para modificar");
        }
        validar(p);
        verificarCodigoUnico(p.getCodigoBarras(), p.getIdProducto());
        return productoDAO.actualizar(p);
    }

    public boolean desactivar(int idProducto) throws Exception {
        if (idProducto <= 0) {
            throw new NegocioException("Seleccione un producto de la lista para desactivar");
        }
        return productoDAO.desactivar(idProducto);
    }

    // ------------------------------------------------------------------ reglas

    /**
     * CUS-02 A2: datos incompletos o inválidos.
     * Es package-private para poder probarlo con JUnit sin base de datos.
     */
    void validar(Producto p) throws NegocioException {
        if (Validador.vacio(p.getCodigoBarras())) {
            throw new NegocioException("Ingrese el código de barras");
        }
        if (p.getCodigoBarras().trim().length() > 50) {
            throw new NegocioException("El código de barras no puede superar los 50 caracteres");
        }
        if (Validador.vacio(p.getNombre())) {
            throw new NegocioException("Ingrese el nombre del producto");
        }
        if (p.getNombre().trim().length() > 120) {
            throw new NegocioException("El nombre no puede superar los 120 caracteres");
        }
        if (p.getDescripcion() != null && p.getDescripcion().length() > 250) {
            throw new NegocioException("La descripción no puede superar los 250 caracteres");
        }
        if (p.getIdCategoria() <= 0) {
            throw new NegocioException("Seleccione una categoría");
        }
        if (!Validador.noNegativo(p.getPrecioCompra())) {
            throw new NegocioException("El precio de compra debe ser mayor o igual a 0");
        }
        if (p.getPrecioVenta() == null || p.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("El precio de venta debe ser mayor a 0");
        }
        if (p.getPrecioVenta().compareTo(p.getPrecioCompra()) < 0) {
            throw new NegocioException("El precio de venta no puede ser menor que el precio de compra");
        }
        if (p.getStockMinimo() < 0) {
            throw new NegocioException("El stock mínimo no puede ser negativo");
        }
    }

    /** CUS-02 A1: código de barras duplicado. idActual = 0 cuando es un producto nuevo. */
    private void verificarCodigoUnico(String codigo, int idActual) throws Exception {
        Producto existente = productoDAO.buscarPorCodigo(codigo);
        if (existente != null && existente.getIdProducto() != idActual) {
            String extra = existente.isEstado() ? "" : " (producto desactivado)";
            throw new NegocioException("El código de barras ya pertenece a otro producto: "
                    + existente.getNombre() + extra);
        }
    }
}
