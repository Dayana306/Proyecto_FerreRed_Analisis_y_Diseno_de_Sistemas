package com.ferrered;

import com.ferrered.capa.presentacion.FrmLogin;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/** Punto de entrada de FerreRed. */
public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // se usa el look and feel por defecto
            }
            aplicarFuenteLegible(15f);   // RSU / RNF-03: texto más grande y legible
            new FrmLogin().setVisible(true);
        });
    }

    private static void aplicarFuenteLegible(float tamano) {
        var keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object k = keys.nextElement();
            Object v = UIManager.get(k);
            if (v instanceof FontUIResource f) {
                UIManager.put(k, new FontUIResource(f.deriveFont(tamano)));
            }
        }
    }
}
