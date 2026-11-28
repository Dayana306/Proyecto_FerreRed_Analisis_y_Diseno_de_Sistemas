package com.ferrered.capa.negocio.util;

import java.math.BigDecimal;

/** Validaciones reutilizables por todos los controladores. */
public final class Validador {

    private Validador() {
    }

    public static boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean noNegativo(BigDecimal v) {
        return v != null && v.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean esDni(String s) {
        return s != null && s.matches("\\d{8}");
    }

    public static boolean esRuc(String s) {
        return s != null && s.matches("\\d{11}");
    }

    /**
     * Convierte texto a decimal (acepta coma o punto). Lanza NegocioException
     * con el nombre del campo si el valor no es numérico.
     */
    public static BigDecimal aDecimal(String texto, String campo) throws NegocioException {
        if (vacio(texto)) {
            throw new NegocioException("El campo " + campo + " es obligatorio");
        }
        try {
            return new BigDecimal(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new NegocioException("El campo " + campo + " debe ser un número válido");
        }
    }

    /** Convierte texto a entero. Lanza NegocioException si no es un número entero. */
    public static int aEntero(String texto, String campo) throws NegocioException {
        if (vacio(texto)) {
            throw new NegocioException("El campo " + campo + " es obligatorio");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new NegocioException("El campo " + campo + " debe ser un número entero");
        }
    }
}
