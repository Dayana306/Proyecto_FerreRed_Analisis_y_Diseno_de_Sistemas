package com.ferrered.capa.negocio.util;

import com.ferrered.capa.datos.entidades.Usuario;

/** Usuario autenticado durante la ejecución (RNF-02: permisos por rol). */
public final class Sesion {

    public static final String ROL_ADMIN = "Administrador";
    public static final String ROL_CAJERO = "Cajero";

    private static Usuario usuarioActual;

    private Sesion() {
    }

    public static void iniciar(Usuario u) {
        usuarioActual = u;
    }

    public static void cerrar() {
        usuarioActual = null;
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static boolean esAdmin() {
        return usuarioActual != null && usuarioActual.getRol() != null
                && ROL_ADMIN.equalsIgnoreCase(usuarioActual.getRol().getNombre());
    }
}
