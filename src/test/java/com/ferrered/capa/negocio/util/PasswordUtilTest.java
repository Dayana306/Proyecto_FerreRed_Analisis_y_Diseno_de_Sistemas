package com.ferrered.capa.negocio.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordUtilTest {

    @Test
    void validaHash() {
        String h = PasswordUtil.sha256("admin123");
        assertTrue(PasswordUtil.coincide("admin123", h));
        assertFalse(PasswordUtil.coincide("otra", h));
    }
}
