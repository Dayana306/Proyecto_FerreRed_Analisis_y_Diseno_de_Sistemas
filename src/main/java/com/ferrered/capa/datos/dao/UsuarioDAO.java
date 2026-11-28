package com.ferrered.capa.datos.dao;

import com.ferrered.capa.datos.entidades.Rol;
import com.ferrered.capa.datos.entidades.Usuario;
import java.util.List;

/**
 * DAO de Usuario. Responsable: Módulo A - Seguridad y Catálogo.
 */
public interface UsuarioDAO extends CrudDAO<Usuario> {

    /** CUS-01: busca el usuario por su nombre de usuario e incluye su Rol. */
    Usuario buscarPorUsuario(String usuario) throws Exception;

    /** RF-13: para validar que el DNI no esté repetido. */
    Usuario buscarPorDni(String dni) throws Exception;

    List<Rol> listarRoles() throws Exception;

    /** RF-13: activar (true) o desactivar (false) una cuenta. */
    boolean cambiarEstado(int idUsuario, boolean estado) throws Exception;

    /** Cantidad de usuarios activos con rol Administrador (para no dejar el sistema sin admin). */
    int contarAdminsActivos() throws Exception;
}
