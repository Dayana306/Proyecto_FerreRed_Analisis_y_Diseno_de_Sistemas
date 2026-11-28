package com.ferrered.capa.presentacion;

import com.ferrered.capa.datos.entidades.Usuario;
import com.ferrered.capa.negocio.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * Menú principal con permisos por rol (RNF-02). Responsable: Módulo A - Seguridad y Catálogo.
 * Administrador: todos los módulos. Cajero: Ventas y Alertas de stock.
 */
public class FrmPrincipal extends JFrame {

    public FrmPrincipal() {
        super("FerreRed - Menú principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Usuario u = Sesion.getUsuario();
        String rol = (u != null && u.getRol() != null) ? u.getRol().getNombre() : "-";
        JLabel lbl = new JLabel("Bienvenido(a): " + (u != null ? u.getNombres() : "") + "   |   Rol: " + rol);
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        add(lbl, BorderLayout.NORTH);

        boolean admin = Sesion.esAdmin();
        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));
        grid.add(boton("Ventas", true, FrmVentas::new));
        grid.add(boton("Alertas de stock", true, FrmInventario::new));
        grid.add(boton("Productos", admin, FrmProductos::new));
        grid.add(boton("Compras", admin, FrmCompras::new));
        grid.add(boton("Proveedores", admin, FrmProveedores::new));
        grid.add(boton("Devoluciones / Anulaciones", admin, FrmDevoluciones::new));
        grid.add(boton("Reportes de ventas", admin, FrmReportes::new));
        grid.add(boton("Usuarios", admin, FrmUsuarios::new));
        JButton salir = new JButton("Cerrar sesión");
        salir.addActionListener(e -> {
            Sesion.cerrar();
            new FrmLogin().setVisible(true);
            dispose();
        });
        grid.add(salir);
        add(grid, BorderLayout.CENTER);
    }

    private JButton boton(String texto, boolean habilitado, Supplier<JFrame> pantalla) {
        JButton b = new JButton(texto);
        b.setEnabled(habilitado);
        b.addActionListener(e -> pantalla.get().setVisible(true));
        return b;
    }
}
