package com.ferrered.capa.presentacion;

/**
 * RF-13: registrar usuarios y asignar rol (Administrador / Cajero).
 * Responsable: Módulo A - Seguridad y Catálogo.
 * Debe comunicarse SOLO con su controlador de capa.negocio (nunca con DAO directamente).
 */
public class FrmUsuarios extends FrmBase {

    public FrmUsuarios() {
        super("Usuarios y Roles");
        // TODO Módulo A - Seguridad y Catálogo: reemplazar el placeholder por los componentes reales
        enConstruccion("Módulo A - Seguridad y Catálogo", "RF-13: registrar usuarios y asignar rol (Administrador / Cajero).");
    }
}
