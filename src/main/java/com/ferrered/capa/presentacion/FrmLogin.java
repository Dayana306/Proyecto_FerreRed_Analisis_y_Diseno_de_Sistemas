package com.ferrered.capa.presentacion;

import com.ferrered.capa.datos.entidades.Usuario;
import com.ferrered.capa.negocio.UsuarioController;
import com.ferrered.capa.negocio.util.NegocioException;

import javax.swing.*;
import java.awt.*;

/**
 * CUS-01 Autentificar usuarios. Responsable: Módulo A - Seguridad y Catálogo.
 * Funcional de punta a punta: Vista -> UsuarioController -> UsuarioDAO -> PostgreSQL.
 */
public class FrmLogin extends JFrame {

    private final JTextField txtUsuario = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnIngresar = new JButton("Iniciar Sesión");
    private final UsuarioController controller = new UsuarioController();

    public FrmLogin() {
        super("FerreRed - Iniciar sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.anchor = GridBagConstraints.WEST;

        JLabel lblU = new JLabel("Usuario:");
        lblU.setDisplayedMnemonic('U');
        lblU.setLabelFor(txtUsuario);
        JLabel lblP = new JLabel("Contraseña:");
        lblP.setDisplayedMnemonic('C');
        lblP.setLabelFor(txtPassword);

        c.gridx = 0; c.gridy = 0; p.add(lblU, c);
        c.gridx = 1; p.add(txtUsuario, c);
        c.gridx = 0; c.gridy = 1; p.add(lblP, c);
        c.gridx = 1; p.add(txtPassword, c);
        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.EAST; p.add(btnIngresar, c);

        add(p);
        getRootPane().setDefaultButton(btnIngresar);   // Enter = ingresar
        btnIngresar.addActionListener(e -> ingresar());
        pack();
        setLocationRelativeTo(null);
    }

    private void ingresar() {
        try {
            Usuario u = controller.autenticar(txtUsuario.getText(), new String(txtPassword.getPassword()));
            new FrmPrincipal().setVisible(true);
            dispose();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso", JOptionPane.WARNING_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
