package com.ferrered.capa.datos.dao;

import com.ferrered.capa.datos.entidades.Producto;
import java.util.List;

/**
 * DAO de Producto. Responsable: Módulo A - Seguridad y Catálogo.
 */
public interface ProductoDAO extends CrudDAO<Producto> {

    /** CUS-02 / CUS-04: búsqueda por código de barras (lector USB). */
    Producto buscarPorCodigo(String codigoBarras) throws Exception;

    /** RF-02: búsqueda por nombre o categoría. */
    List<Producto> buscar(String texto, Integer idCategoria) throws Exception;

    /** CUS-02: inserta producto + registro inicial de inventario en una transacción. */
    int insertarConInventario(Producto p, int stockInicial, int stockMinimo) throws Exception;
}
