package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.conexion.Conexion;
import com.ferrered.capa.datos.dao.UsuarioDAO;
import com.ferrered.capa.datos.entidades.Rol;
import com.ferrered.capa.datos.entidades.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de UsuarioDAO. Responsable: Módulo A - Seguridad y Catálogo.
 * buscarPorUsuario y listarRoles ya están implementados: sirven de EJEMPLO
 * para el resto de DAOs del equipo.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario buscarPorUsuario(String usuario) throws Exception {
        String sql = "SELECT u.*, r.nombre AS rol_nombre, r.descripcion AS rol_desc, r.estado AS rol_estado "
                + "FROM usuario u JOIN rol r ON r.id_rol = u.id_rol WHERE u.usuario = ?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));
                u.setDni(rs.getString("dni"));
                u.setUsuario(rs.getString("usuario"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setEmail(rs.getString("email"));
                u.setEstado(rs.getBoolean("estado"));
                u.setIdRol(rs.getInt("id_rol"));

                Rol r = new Rol();
                r.setIdRol(u.getIdRol());
                r.setNombre(rs.getString("rol_nombre"));
                r.setDescripcion(rs.getString("rol_desc"));
                r.setEstado(rs.getBoolean("rol_estado"));
                u.setRol(r);
                return u;
            }
        }
    }

    @Override
    public List<Rol> listarRoles() throws Exception {
        List<Rol> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM rol WHERE estado = true ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Rol r = new Rol();
                r.setIdRol(rs.getInt("id_rol"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setEstado(rs.getBoolean("estado"));
                lista.add(r);
            }
        }
        return lista;
    }

    // ----- RF-13 Gestionar usuarios (pendiente) -----

    @Override
    public int insertar(Usuario entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public boolean actualizar(Usuario entidad) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public boolean desactivar(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public Usuario buscarPorId(int id) throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }

    @Override
    public List<Usuario> listar() throws Exception {
        throw new UnsupportedOperationException("TODO Módulo A - Seguridad y Catálogo");
    }
}
