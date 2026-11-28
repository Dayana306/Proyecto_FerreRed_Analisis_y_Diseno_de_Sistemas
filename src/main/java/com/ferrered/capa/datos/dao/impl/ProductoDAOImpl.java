package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.conexion.Conexion;
import com.ferrered.capa.datos.dao.ProductoDAO;
import com.ferrered.capa.datos.entidades.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de ProductoDAO. Responsable: Módulo A - Seguridad y Catálogo.
 *
 * Todas las consultas traen además el stock (tabla inventario) y el nombre de la
 * categoría, para mostrarlos en pantalla sin consultas extra.
 */
public class ProductoDAOImpl implements ProductoDAO {

    /** SELECT base: producto + categoría + inventario. */
    private static final String SELECT_BASE =
            "SELECT p.id_producto, p.codigo_barras, p.nombre, p.descripcion, p.precio_compra, "
                    + "p.precio_venta, p.estado, p.id_categoria, c.nombre AS nombre_categoria, "
                    + "COALESCE(i.stock_actual, 0) AS stock_actual, COALESCE(i.stock_minimo, 0) AS stock_minimo "
                    + "FROM producto p "
                    + "JOIN categoria c ON c.id_categoria = p.id_categoria "
                    + "LEFT JOIN inventario i ON i.id_producto = p.id_producto ";

    // ------------------------------------------------------------------ consultas

    /**
     * Busca por código de barras SIN filtrar por estado (sirve para validar duplicados).
     * Quien lo use para vender/comprar (Módulos B y C) debe verificar p.isEstado().
     */
    @Override
    public Producto buscarPorCodigo(String codigoBarras) throws Exception {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BASE + "WHERE p.codigo_barras = ?")) {
            ps.setString(1, codigoBarras.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public Producto buscarPorId(int id) throws Exception {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BASE + "WHERE p.id_producto = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /** Productos activos ordenados por nombre. */
    @Override
    public List<Producto> listar() throws Exception {
        return buscar(null, null);
    }

    /**
     * RF-02: busca productos activos por nombre o código (coincidencia parcial,
     * sin importar mayúsculas) y opcionalmente por categoría. null = sin filtro.
     */
    @Override
    public List<Producto> buscar(String texto, Integer idCategoria) throws Exception {
        StringBuilder sql = new StringBuilder(SELECT_BASE).append("WHERE p.estado = TRUE ");
        List<Object> params = new ArrayList<>();

        if (texto != null && !texto.trim().isEmpty()) {
            sql.append("AND (p.nombre ILIKE ? OR p.codigo_barras ILIKE ?) ");
            String patron = "%" + texto.trim() + "%";
            params.add(patron);
            params.add(patron);
        }
        if (idCategoria != null && idCategoria > 0) {
            sql.append("AND p.id_categoria = ? ");
            params.add(idCategoria);
        }
        sql.append("ORDER BY p.nombre");

        List<Producto> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    // ------------------------------------------------------------------ escritura

    /** Usa stockActual y stockMinimo del propio producto como valores iniciales. */
    @Override
    public int insertar(Producto p) throws Exception {
        return insertarConInventario(p, p.getStockActual(), p.getStockMinimo());
    }

    /**
     * CUS-02 paso 7: registra el producto y su inventario inicial en UNA transacción.
     * Si falla cualquiera de los dos INSERT, no se guarda nada (rollback).
     */
    @Override
    public int insertarConInventario(Producto p, int stockInicial, int stockMinimo) throws Exception {
        String sqlProd = "INSERT INTO producto (codigo_barras, nombre, descripcion, precio_compra, "
                + "precio_venta, id_categoria) VALUES (?, ?, ?, ?, ?, ?) RETURNING id_producto";
        String sqlInv = "INSERT INTO inventario (id_producto, stock_actual, stock_minimo) VALUES (?, ?, ?)";

        try (Connection cn = Conexion.getConexion()) {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sqlProd);
                 PreparedStatement pi = cn.prepareStatement(sqlInv)) {
                ps.setString(1, p.getCodigoBarras().trim());
                ps.setString(2, p.getNombre().trim());
                ps.setString(3, p.getDescripcion());
                ps.setBigDecimal(4, p.getPrecioCompra());
                ps.setBigDecimal(5, p.getPrecioVenta());
                ps.setInt(6, p.getIdCategoria());
                int id;
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    id = rs.getInt(1);
                }

                pi.setInt(1, id);
                pi.setInt(2, stockInicial);
                pi.setInt(3, stockMinimo);
                pi.executeUpdate();

                cn.commit();
                p.setIdProducto(id);
                return id;
            } catch (Exception e) {
                cn.rollback();
                throw e;
            }
        }
    }

    /**
     * Actualiza los datos del producto y su stock mínimo en UNA transacción.
     * El stock ACTUAL no se edita aquí: solo cambia por compras, ventas y devoluciones.
     */
    @Override
    public boolean actualizar(Producto p) throws Exception {
        String sqlProd = "UPDATE producto SET codigo_barras = ?, nombre = ?, descripcion = ?, "
                + "precio_compra = ?, precio_venta = ?, id_categoria = ? WHERE id_producto = ?";
        String sqlInv = "UPDATE inventario SET stock_minimo = ?, fecha_actualizacion = CURRENT_TIMESTAMP "
                + "WHERE id_producto = ?";

        try (Connection cn = Conexion.getConexion()) {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sqlProd);
                 PreparedStatement pi = cn.prepareStatement(sqlInv)) {
                ps.setString(1, p.getCodigoBarras().trim());
                ps.setString(2, p.getNombre().trim());
                ps.setString(3, p.getDescripcion());
                ps.setBigDecimal(4, p.getPrecioCompra());
                ps.setBigDecimal(5, p.getPrecioVenta());
                ps.setInt(6, p.getIdCategoria());
                ps.setInt(7, p.getIdProducto());
                int filas = ps.executeUpdate();

                pi.setInt(1, p.getStockMinimo());
                pi.setInt(2, p.getIdProducto());
                pi.executeUpdate();

                cn.commit();
                return filas > 0;
            } catch (Exception e) {
                cn.rollback();
                throw e;
            }
        }
    }

    /** Eliminación lógica: el producto deja de listarse pero conserva su historial de ventas. */
    @Override
    public boolean desactivar(int id) throws Exception {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE producto SET estado = FALSE WHERE id_producto = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ------------------------------------------------------------------ mapeo

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setCodigoBarras(rs.getString("codigo_barras"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecioCompra(rs.getBigDecimal("precio_compra"));
        p.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        p.setEstado(rs.getBoolean("estado"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setNombreCategoria(rs.getString("nombre_categoria"));
        p.setStockActual(rs.getInt("stock_actual"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        return p;
    }
}
