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
}
