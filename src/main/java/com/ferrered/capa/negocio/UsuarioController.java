package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.UsuarioDAO;
import com.ferrered.capa.datos.dao.impl.UsuarioDAOImpl;
import com.ferrered.capa.datos.entidades.Usuario;
import com.ferrered.capa.negocio.util.NegocioException;
import com.ferrered.capa.negocio.util.PasswordUtil;
import com.ferrered.capa.negocio.util.Sesion;
import com.ferrered.capa.negocio.util.Validador;

/**
 * CUS-01 Autentificar usuarios / RF-13 Gestionar usuarios. Responsable: Módulo A - Seguridad y Catálogo.
 * autenticar() está implementado como ejemplo de controlador.
 */
public class UsuarioController {

    private final UsuarioDAO dao;

    public UsuarioController() {
        this(new UsuarioDAOImpl());
    }

    public UsuarioController(UsuarioDAO dao) {
        this.dao = dao;
    }

    public Usuario autenticar(String usuario, String password) throws Exception {
        if (Validador.vacio(usuario) || Validador.vacio(password)) {
            throw new NegocioException("Ingrese usuario y contraseña");
        }
        Usuario u = dao.buscarPorUsuario(usuario.trim());
        if (u == null || !PasswordUtil.coincide(password, u.getPasswordHash())) {
            throw new NegocioException("Usuario o contraseña incorrecta");          // A1
        }
        if (!u.isEstado()) {
            throw new NegocioException("Cuenta desactivada, contacte con el administrador"); // A2
        }
        Sesion.iniciar(u);
        return u;
    }

    // TODO Módulo A - Seguridad y Catálogo: registrar, modificar, desactivar y listar usuarios (RF-13)
}
