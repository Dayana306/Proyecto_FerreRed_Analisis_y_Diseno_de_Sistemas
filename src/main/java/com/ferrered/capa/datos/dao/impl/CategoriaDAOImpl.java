package com.ferrered.capa.datos.dao.impl;

import com.ferrered.capa.datos.conexion.Conexion;
import com.ferrered.capa.datos.dao.CategoriaDAO;
import com.ferrered.capa.datos.entidades.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de CategoriaDAO. Responsable: Módulo A - Seguridad y Catálogo.
 */
public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public int insertar(Categoria c) throws Exception {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?) RETURNING id_categoria";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    @Override
    public boolean actualizar(Categoria c) throws Exception {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getIdCategoria());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean desactivar(int id) throws Exception {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE categoria SET estado = FALSE WHERE id_categoria = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Categoria buscarPorId(int id) throws Exception {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM categoria WHERE id_categoria = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /** Solo categorías activas, ordenadas por nombre (para los combos). */
    @Override
    public List<Categoria> listar() throws Exception {
        List<Categoria> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM categoria WHERE estado = TRUE ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setEstado(rs.getBoolean("estado"));
        return c;
    }
}
