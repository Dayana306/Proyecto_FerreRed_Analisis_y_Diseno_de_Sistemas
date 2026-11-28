package com.ferrered.capa.negocio.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Hash SHA-256 para contraseñas (CUS-01: validación contra password_hash). */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean coincide(String plano, String hash) {
        return hash != null && hash.equalsIgnoreCase(sha256(plano));
    }
}
