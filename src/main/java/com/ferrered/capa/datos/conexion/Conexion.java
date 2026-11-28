package com.ferrered.capa.datos.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Proporciona conexiones JDBC a PostgreSQL leyendo src/main/resources/db.properties.
 * Uso: try (Connection cn = Conexion.getConexion()) { ... }
 */
public final class Conexion {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Conexion.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró db.properties");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Error leyendo db.properties", e);
        }
    }

    private Conexion() {
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }
}
