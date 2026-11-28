package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.CategoriaDAO;
import com.ferrered.capa.datos.dao.ProductoDAO;
import com.ferrered.capa.datos.dao.impl.CategoriaDAOImpl;
import com.ferrered.capa.datos.dao.impl.ProductoDAOImpl;

/**
 * CUS-02 Gestionar Productos (RF-01, RF-02) + categorías.
 * Responsable: Módulo A - Seguridad y Catálogo.
 */
@SuppressWarnings("unused")
public class ProductoController {

    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

    // TODO: registrar(): validar campos, código de barras no duplicado (A1), precios/stock >= 0 (A2), luego insertarConInventario()
    // TODO: modificar(), desactivar(), buscar(texto, idCategoria), listarCategorias()
}
