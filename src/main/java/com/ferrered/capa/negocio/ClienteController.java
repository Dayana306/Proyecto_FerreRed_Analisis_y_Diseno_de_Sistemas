package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.ClienteDAO;
import com.ferrered.capa.datos.dao.impl.ClienteDAOImpl;

/**
 * Clientes usados en CUS-04 (incluye 'Cliente Varios' por defecto).
 * Responsable: Módulo B - Ventas y Devoluciones.
 */
@SuppressWarnings("unused")
public class ClienteController {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();

    // TODO: buscarPorDocumento(), registrarRapido(), obtenerClienteVarios()
}
