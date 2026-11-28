package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.conexion.Conexion;
import com.ferrered.capa.datos.dao.UsuarioDAO;
import com.ferrered.capa.datos.entidades.Rol;
import com.ferrered.capa.datos.entidades.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de UsuarioDAO. Responsable: Módulo A - Seguridad y Catálogo.
 * Las contraseñas llegan aquí YA convertidas a hash (lo hace UsuarioController).
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    /** Nombre del rol administrador tal como está en la tabla rol. */
    private static final String ROL_ADMIN = "Administrador";

    /** SELECT base: usuario + datos de su rol. */
    private static final String SELECT_BASE =
            "SELECT u.id_usuario, u.nombres, u.apellidos, u.dni, u.usuario, u.password_hash, u.email, "
                    + "u.estado, u.id_rol, r.nombre AS rol_nombre, r.descripcion AS rol_desc, r.estado AS rol_estado "
                    + "FROM usuario u JOIN rol r ON r.id_rol = u.id_rol ";

    // ------------------------------------------------------------------ consultas

    @Override
    public Usuario buscarPorUsuario(String usuario) throws Exception {
        return buscarUno("WHERE u.usuario = ?", usuario);
    }

    @Override
    public Usuario buscarPorDni(String dni) throws Exception {
        return buscarUno("WHERE u.dni = ?", dni);
    }

    @Override
    public Usuario buscarPorId(int id) throws Exception {
        return buscarUno("WHERE u.id_usuario = ?", id);
    }

    /** Todos los usuarios (activos e inactivos), para que el administrador pueda reactivarlos. */
    @Override
    public List<Usuario> listar() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BASE + "ORDER BY u.estado DESC, u.apellidos, u.nombres");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Rol> listarRoles() throws Exception {
        List<Rol> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM rol WHERE estado = TRUE ORDER BY nombre");
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

    @Override
    public int contarAdminsActivos() throws Exception {
        String sql = "SELECT COUNT(*) FROM usuario u JOIN rol r ON r.id_rol = u.id_rol "
                + "WHERE u.estado = TRUE AND r.nombre = ?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ROL_ADMIN);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // ------------------------------------------------------------------ escritura

    @Override
    public int insertar(Usuario u) throws Exception {
        String sql = "INSERT INTO usuario (nombres, apellidos, dni, usuario, password_hash, email, id_rol) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_usuario";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getNombres());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getDni());
            ps.setString(4, u.getUsuario());
            ps.setString(5, u.getPasswordHash());
            ps.setString(6, u.getEmail());
            ps.setInt(7, u.getIdRol());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int id = rs.getInt(1);
                u.setIdUsuario(id);
                return id;
            }
        }
    }

    /**
     * Actualiza datos y rol. La contraseña solo se cambia si passwordHash != null
     * (cuando el administrador deja el campo vacío, se conserva la anterior).
     */
    @Override
    public boolean actualizar(Usuario u) throws Exception {
        boolean cambiaPassword = u.getPasswordHash() != null;
        String sql = "UPDATE usuario SET nombres = ?, apellidos = ?, dni = ?, usuario = ?, email = ?, id_rol = ?"
                + (cambiaPassword ? ", password_hash = ?" : "")
                + " WHERE id_usuario = ?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, u.getNombres());
            ps.setString(i++, u.getApellidos());
            ps.setString(i++, u.getDni());
            ps.setString(i++, u.getUsuario());
            ps.setString(i++, u.getEmail());
            ps.setInt(i++, u.getIdRol());
            if (cambiaPassword) {
                ps.setString(i++, u.getPasswordHash());
            }
            ps.setInt(i, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarEstado(int idUsuario, boolean estado) throws Exception {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("UPDATE usuario SET estado = ? WHERE id_usuario = ?")) {
            ps.setBoolean(1, estado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean desactivar(int id) throws Exception {
        return cambiarEstado(id, false);
    }

    // ------------------------------------------------------------------ auxiliares

    private Usuario buscarUno(String where, Object valor) throws Exception {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BASE + where)) {
            ps.setObject(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
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
