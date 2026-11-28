package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.ProveedorDAO;
import com.ferrered.capa.datos.dao.impl.ProveedorDAOImpl;

/**
 * RF-10 Gestionar proveedores.
 * Responsable: Módulo C - Compras, Inventario y Reportes.
 */
@SuppressWarnings("unused")
public class ProveedorController {

    private final ProveedorDAO proveedorDAO = new ProveedorDAOImpl();

    // TODO: registrar() validando RUC de 11 dígitos, modificar(), desactivar(), listar()
}
