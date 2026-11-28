package com.ferrered.capa.negocio;

import com.ferrered.capa.datos.dao.UsuarioDAO;
import com.ferrered.capa.datos.dao.impl.UsuarioDAOImpl;
import com.ferrered.capa.datos.entidades.Rol;
import com.ferrered.capa.datos.entidades.Usuario;
import com.ferrered.capa.negocio.util.NegocioException;
import com.ferrered.capa.negocio.util.PasswordUtil;
import com.ferrered.capa.negocio.util.Sesion;
import com.ferrered.capa.negocio.util.Validador;
import java.util.ArrayList;
import java.util.List;

/**
 * CUS-01 Autentificar usuarios / RF-13 Gestionar usuarios y roles.
 * Responsable: Módulo A - Seguridad y Catálogo.
 */
public class UsuarioController {

    public static final int MIN_PASSWORD = 6;

    private final UsuarioDAO dao;

    public UsuarioController() {
        this(new UsuarioDAOImpl());
    }

    /** Constructor para pruebas unitarias. */
    public UsuarioController(UsuarioDAO dao) {
        this.dao = dao;
    }

    // ================================================================== CUS-01

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

    // ================================================================== RF-13 consultas

    public List<Usuario> listar() throws Exception {
        return dao.listar();
    }

    public List<Rol> listarRoles() throws Exception {
        return dao.listarRoles();
    }

    /** Filtra en memoria por nombres, apellidos, usuario o DNI (los usuarios son pocos). */
    public List<Usuario> filtrar(List<Usuario> usuarios, String texto) {
        if (Validador.vacio(texto)) {
            return usuarios;
        }
        String t = texto.trim().toLowerCase();
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario u : usuarios) {
            String datos = (u.getNombres() + " " + u.getApellidos() + " " + u.getUsuario() + " "
                    + (u.getDni() == null ? "" : u.getDni())).toLowerCase();
            if (datos.contains(t)) {
                resultado.add(u);
            }
        }
        return resultado;
    }

    // ================================================================== RF-13 operaciones

    public int registrar(Usuario u, String password, String confirmacion) throws Exception {
        normalizar(u);
        validar(u, password, confirmacion, true);
        verificarDuplicados(u, 0);
        u.setPasswordHash(PasswordUtil.sha256(password));
        return dao.insertar(u);
    }

    /** Si password viene vacío, se conserva la contraseña actual. */
    public boolean modificar(Usuario u, String password, String confirmacion) throws Exception {
        if (u.getIdUsuario() <= 0) {
            throw new NegocioException("Seleccione un usuario de la lista para modificar");
        }
        normalizar(u);
        validar(u, password, confirmacion, false);
        verificarDuplicados(u, u.getIdUsuario());

        Usuario actual = dao.buscarPorId(u.getIdUsuario());
        boolean eraAdmin = actual != null && esAdmin(actual);
        if (eraAdmin && !esAdmin(u)) {
            if (esUsuarioEnSesion(u.getIdUsuario())) {
                throw new NegocioException("No puede quitarse a sí mismo el rol de Administrador");
            }
            if (actual.isEstado() && dao.contarAdminsActivos() <= 1) {
                throw new NegocioException("Debe existir al menos un Administrador activo");
            }
        }

        u.setPasswordHash(Validador.vacio(password) ? null : PasswordUtil.sha256(password));
        boolean ok = dao.actualizar(u);

        if (ok && esUsuarioEnSesion(u.getIdUsuario())) {
            Sesion.iniciar(dao.buscarPorId(u.getIdUsuario()));   // refresca nombre/rol en sesión
        }
        return ok;
    }

    /** Activa o desactiva una cuenta. Un usuario desactivado no puede iniciar sesión (CUS-01 A2). */
    public boolean cambiarEstado(int idUsuario, boolean activar) throws Exception {
        if (idUsuario <= 0) {
            throw new NegocioException("Seleccione un usuario de la lista");
        }
        if (!activar) {
            if (esUsuarioEnSesion(idUsuario)) {
                throw new NegocioException("No puede desactivar su propia cuenta");
            }
            Usuario u = dao.buscarPorId(idUsuario);
            if (u != null && esAdmin(u) && u.isEstado() && dao.contarAdminsActivos() <= 1) {
                throw new NegocioException("Debe existir al menos un Administrador activo");
            }
        }
        return dao.cambiarEstado(idUsuario, activar);
    }

    // ================================================================== reglas

    /** Validaciones de formato. Package-private para probarlas con JUnit sin base de datos. */
    void validar(Usuario u, String password, String confirmacion, boolean esNuevo) throws NegocioException {
        validarNombre(u.getNombres(), "nombres");
        validarNombre(u.getApellidos(), "apellidos");

        if (!Validador.esDni(u.getDni())) {
            throw new NegocioException("El DNI debe tener 8 dígitos");
        }
        if (u.getEmail() != null) {
            if (u.getEmail().length() > 100 || !u.getEmail().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                throw new NegocioException("Ingrese un correo electrónico válido");
            }
        }
        if (Validador.vacio(u.getUsuario()) || !u.getUsuario().matches("^[A-Za-z0-9._]{4,50}$")) {
            throw new NegocioException("El usuario debe tener entre 4 y 50 caracteres "
                    + "(letras, números, punto o guion bajo, sin espacios)");
        }
        if (u.getIdRol() <= 0) {
            throw new NegocioException("Seleccione un rol");
        }

        boolean tienePassword = !Validador.vacio(password);
        if (esNuevo && !tienePassword) {
            throw new NegocioException("Ingrese una contraseña");
        }
        if (tienePassword) {
            if (password.length() < MIN_PASSWORD) {
                throw new NegocioException("La contraseña debe tener al menos " + MIN_PASSWORD + " caracteres");
            }
            if (!password.equals(confirmacion)) {
                throw new NegocioException("Las contraseñas no coinciden");
            }
        }
    }

    private void validarNombre(String valor, String campo) throws NegocioException {
        if (Validador.vacio(valor)) {
            throw new NegocioException("Ingrese los " + campo);
        }
        if (valor.length() > 80 || !valor.matches("^[\\p{L} .'-]+$")) {
            throw new NegocioException("Los " + campo + " solo pueden contener letras (máx. 80 caracteres)");
        }
    }

    private void verificarDuplicados(Usuario u, int idActual) throws Exception {
        Usuario mismoUsuario = dao.buscarPorUsuario(u.getUsuario());
        if (mismoUsuario != null && mismoUsuario.getIdUsuario() != idActual) {
            throw new NegocioException("El nombre de usuario \"" + u.getUsuario() + "\" ya está en uso");
        }
        Usuario mismoDni = dao.buscarPorDni(u.getDni());
        if (mismoDni != null && mismoDni.getIdUsuario() != idActual) {
            throw new NegocioException("El DNI ya está registrado para el usuario " + mismoDni.getUsuario());
        }
    }

    /** Quita espacios sobrantes; el correo vacío se guarda como null. */
    private void normalizar(Usuario u) {
        u.setNombres(u.getNombres() == null ? null : u.getNombres().trim());
        u.setApellidos(u.getApellidos() == null ? null : u.getApellidos().trim());
        u.setDni(u.getDni() == null ? null : u.getDni().trim());
        u.setUsuario(u.getUsuario() == null ? null : u.getUsuario().trim());
        u.setEmail(Validador.vacio(u.getEmail()) ? null : u.getEmail().trim());
    }

    private boolean esAdmin(Usuario u) {
        return u.getRol() != null && Sesion.ROL_ADMIN.equalsIgnoreCase(u.getRol().getNombre());
    }

    private boolean esUsuarioEnSesion(int idUsuario) {
        return Sesion.getUsuario() != null && Sesion.getUsuario().getIdUsuario() == idUsuario;
    }
}
