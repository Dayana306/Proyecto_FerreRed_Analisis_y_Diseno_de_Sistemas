package com.ferrered.capa.negocio.util;

/** Error de regla de negocio: su mensaje se muestra tal cual al usuario. */
public class NegocioException extends Exception {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
