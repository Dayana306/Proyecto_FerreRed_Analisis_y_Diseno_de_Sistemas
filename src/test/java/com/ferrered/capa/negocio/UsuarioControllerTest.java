package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.entidades.Usuario;
import com.ferrered.capa.negocio.util.NegocioException;
import com.ferrered.capa.negocio.util.Sesion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de RF-13 (validaciones de usuario). No necesitan base de datos.
 */
class UsuarioControllerTest {

    private UsuarioController controller;
    private Usuario u;

    @BeforeEach
    void preparar() {
        controller = new UsuarioController(null);
        u = new Usuario();
        u.setNombres("Rosa María");
        u.setApellidos("Quispe Huamán");
        u.setDni("45678912");
        u.setEmail("rosa@ferrered.pe");
        u.setUsuario("rquispe");
        u.setIdRol(2);
    }

    @AfterEach
    void limpiar() {
        Sesion.cerrar();
    }

    @Test
    void usuarioNuevoValidoPasa() {
        assertDoesNotThrow(() -> controller.validar(u, "clave123", "clave123", true));
    }

    @Test
    void usuarioNuevoSinPasswordFalla() {
        assertThrows(NegocioException.class, () -> controller.validar(u, "", "", true));
    }

    @Test
    void edicionSinPasswordPasa() {
        assertDoesNotThrow(() -> controller.validar(u, "", "", false));
    }

    @Test
    void passwordCortaFalla() {
        assertThrows(NegocioException.class, () -> controller.validar(u, "abc", "abc", true));
    }

    @Test
    void passwordsDistintasFalla() {
        NegocioException ex = assertThrows(NegocioException.class,
                () -> controller.validar(u, "clave123", "clave124", true));
        assertEquals("Las contraseñas no coinciden", ex.getMessage());
    }

    @Test
    void dniInvalidoFalla() {
        u.setDni("1234");
        assertThrows(NegocioException.class, () -> controller.validar(u, "clave123", "clave123", true));
    }

    @Test
    void usuarioConEspaciosFalla() {
        u.setUsuario("rosa q");
        assertThrows(NegocioException.class, () -> controller.validar(u, "clave123", "clave123", true));
    }

    @Test
    void correoInvalidoFalla() {
        u.setEmail("rosa@");
        assertThrows(NegocioException.class, () -> controller.validar(u, "clave123", "clave123", true));
    }

    @Test
    void noPuedeDesactivarseASiMismo() {
        Usuario admin = new Usuario();
        admin.setIdUsuario(1);
        Sesion.iniciar(admin);
        NegocioException ex = assertThrows(NegocioException.class, () -> controller.cambiarEstado(1, false));
        assertEquals("No puede desactivar su propia cuenta", ex.getMessage());
    }

    @Test
    void filtraPorNombreUsuarioODni() {
        u.setIdUsuario(5);
        List<Usuario> lista = List.of(u);
        assertEquals(1, controller.filtrar(lista, "quispe").size());
        assertEquals(1, controller.filtrar(lista, "45678").size());
        assertEquals(0, controller.filtrar(lista, "pedro").size());
    }
}
