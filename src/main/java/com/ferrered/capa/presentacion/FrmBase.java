package com.ferrered.capa.presentacion;

import javax.swing.*;
import java.awt.*;

/**
 * Base común de los formularios: tamaño, centrado y mensajes estándar.
 * Mantener la misma apariencia en todos los módulos (RNF-03, RSU accesibilidad).
 */
public abstract class FrmBase extends JFrame {

    protected FrmBase(String titulo) {
        super("FerreRed - " + titulo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        JLabel lbl = new JLabel(titulo, SwingConstants.LEFT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 22f));
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 16, 0, 16));
        add(lbl, BorderLayout.NORTH);
    }

    protected void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirmar(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmar", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    /** Placeholder mientras el módulo se construye. */
    protected void enConstruccion(String responsable, String detalle) {
        JLabel l = new JLabel("<html><center>Pantalla en construcción<br><br><b>" + responsable
                + "</b><br><br>" + detalle + "</center></html>", SwingConstants.CENTER);
        add(l, BorderLayout.CENTER);
    }
}
