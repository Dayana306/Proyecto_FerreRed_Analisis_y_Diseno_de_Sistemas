package com.ferrered.capa.presentacion;

import com.ferrered.capa.datos.entidades.Rol;
import com.ferrered.capa.datos.entidades.Usuario;
import com.ferrered.capa.negocio.UsuarioController;
import com.ferrered.capa.negocio.util.NegocioException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * RF-13 Gestionar usuarios y roles. Responsable: Módulo A - Seguridad y Catálogo.
 *
 * Uso:
 *  - "Nuevo": limpia el formulario para registrar un usuario.
 *  - Clic en una fila: carga el usuario para editarlo.
 *  - Al editar, si la contraseña se deja vacía se conserva la actual.
 *  - "Desactivar"/"Activar": bloquea o habilita el inicio de sesión (CUS-01 A2).
 * Las filas grises son cuentas desactivadas.
 */
public class FrmUsuarios extends FrmBase {

    private static final Color GRIS = new Color(225, 225, 225);

    private final UsuarioController controller = new UsuarioController();

    private List<Usuario> todos = new ArrayList<>();
    private List<Usuario> usuariosEnTabla = new ArrayList<>();
    private Usuario seleccionado = null;            // null = registrando uno nuevo

    // ---- búsqueda ----
    private final JTextField txtBuscar = new JTextField(24);

    // ---- tabla ----
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Usuario", "Nombres", "Apellidos", "DNI", "Rol", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    // ---- formulario ----
    private final JLabel lblModo = new JLabel();
    private final JTextField txtNombres = new JTextField(16);
    private final JTextField txtApellidos = new JTextField(16);
    private final JTextField txtDni = new JTextField(16);
    private final JTextField txtEmail = new JTextField(16);
    private final JTextField txtUsuario = new JTextField(16);
    private final JComboBox<Rol> cboRol = new JComboBox<>();
    private final JPasswordField txtPassword = new JPasswordField(16);
    private final JPasswordField txtConfirmar = new JPasswordField(16);
    private final JLabel lblAyudaPassword = new JLabel();

    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnEstado = new JButton("Desactivar");

    public FrmUsuarios() {
        super("Usuarios y Roles");
        setSize(1100, 620);
        setLocationRelativeTo(null);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        centro.add(crearPanelBusqueda(), BorderLayout.NORTH);
        centro.add(crearPanelTabla(), BorderLayout.CENTER);
        centro.add(crearPanelFormulario(), BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);

        cargarRoles();
        registrarEventos();
        cargarUsuarios();
        modoNuevo();
    }

    // ================================================================== armado de la vista

    private JPanel crearPanelBusqueda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.add(etiqueta("Buscar (nombre, usuario o DNI):", 'B', txtBuscar));
        p.add(txtBuscar);
        return p;
    }

    private JScrollPane crearPanelTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setDefaultRenderer(Object.class, new RenderInactivo());
        return new JScrollPane(tabla);
    }

    private JPanel crearPanelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Datos del usuario"));
        p.setPreferredSize(new Dimension(380, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 8, 5, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;
        c.gridx = 0; c.gridy = fila++; c.gridwidth = 2;
        lblModo.setFont(lblModo.getFont().deriveFont(Font.BOLD));
        p.add(lblModo, c);
        c.gridwidth = 1;

        fila = agregarCampo(p, c, fila, etiqueta("Nombres *", 'N', txtNombres), txtNombres);
        fila = agregarCampo(p, c, fila, etiqueta("Apellidos *", 'A', txtApellidos), txtApellidos);
        fila = agregarCampo(p, c, fila, etiqueta("DNI *", 'D', txtDni), txtDni);
        fila = agregarCampo(p, c, fila, etiqueta("Correo", 'O', txtEmail), txtEmail);
        fila = agregarCampo(p, c, fila, etiqueta("Usuario *", 'U', txtUsuario), txtUsuario);
        fila = agregarCampo(p, c, fila, etiqueta("Rol *", 'R', cboRol), cboRol);
        fila = agregarCampo(p, c, fila, etiqueta("Contraseña", 'C', txtPassword), txtPassword);
        fila = agregarCampo(p, c, fila, etiqueta("Confirmar", 'F', txtConfirmar), txtConfirmar);

        c.gridx = 1; c.gridy = fila++;
        lblAyudaPassword.setFont(lblAyudaPassword.getFont().deriveFont(12f));
        p.add(lblAyudaPassword, c);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEstado);
        c.gridx = 0; c.gridy = fila++; c.gridwidth = 2;
        c.insets = new Insets(16, 8, 5, 8);
        p.add(botones, c);

        c.gridy = fila;
        c.weighty = 1;
        p.add(new JLabel("<html><small>* obligatorio. Filas grises: cuentas desactivadas.</small></html>"), c);
        return p;
    }

    private int agregarCampo(JPanel p, GridBagConstraints c, int fila, JLabel lbl, JComponent campo) {
        c.gridx = 0; c.gridy = fila; c.weightx = 0;
        p.add(lbl, c);
        c.gridx = 1; c.weightx = 1;
        p.add(campo, c);
        return fila + 1;
    }

    /** Etiqueta con atajo de teclado Alt+letra (accesibilidad, RSU). */
    private JLabel etiqueta(String texto, char atajo, JComponent campo) {
        JLabel l = new JLabel(texto);
        l.setDisplayedMnemonic(atajo);
        l.setLabelFor(campo);
        return l;
    }

    private void registrarEventos() {
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { mostrarEnTabla(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { mostrarEnTabla(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { mostrarEnTabla(); }
        });
        btnNuevo.addActionListener(e -> modoNuevo());
        btnGuardar.addActionListener(e -> guardar());
        btnEstado.addActionListener(e -> cambiarEstado());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionado();
            }
        });
    }

    // ================================================================== carga de datos

    private void cargarRoles() {
        try {
            for (Rol r : controller.listarRoles()) {
                cboRol.addItem(r);
            }
        } catch (Exception ex) {
            error("No se pudieron cargar los roles:\n" + ex.getMessage());
        }
    }

    private void cargarUsuarios() {
        try {
            todos = controller.listar();
            mostrarEnTabla();
        } catch (Exception ex) {
            error("No se pudo cargar la lista de usuarios:\n" + ex.getMessage());
        }
    }

    /** Aplica el filtro de búsqueda (en memoria) y llena la tabla. */
    private void mostrarEnTabla() {
        usuariosEnTabla = controller.filtrar(todos, txtBuscar.getText());
        modelo.setRowCount(0);
        for (Usuario u : usuariosEnTabla) {
            modelo.addRow(new Object[]{
                    u.getUsuario(), u.getNombres(), u.getApellidos(), u.getDni(),
                    u.getRol() == null ? "" : u.getRol().getNombre(),
                    u.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    // ================================================================== modos del formulario

    private void modoNuevo() {
        seleccionado = null;
        tabla.clearSelection();
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtUsuario.setText("");
        txtPassword.setText("");
        txtConfirmar.setText("");
        if (cboRol.getItemCount() > 0) {
            cboRol.setSelectedIndex(0);
        }
        lblAyudaPassword.setText("Mínimo " + UsuarioController.MIN_PASSWORD + " caracteres");
        btnEstado.setText("Desactivar");
        btnEstado.setEnabled(false);
        lblModo.setText("Nuevo usuario");
        txtNombres.requestFocus();
    }

    private void cargarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= usuariosEnTabla.size()) {
            return;
        }
        seleccionado = usuariosEnTabla.get(fila);
        txtNombres.setText(seleccionado.getNombres());
        txtApellidos.setText(seleccionado.getApellidos());
        txtDni.setText(seleccionado.getDni() == null ? "" : seleccionado.getDni());
        txtEmail.setText(seleccionado.getEmail() == null ? "" : seleccionado.getEmail());
        txtUsuario.setText(seleccionado.getUsuario());
        txtPassword.setText("");
        txtConfirmar.setText("");
        seleccionarRol(seleccionado.getIdRol());

        lblAyudaPassword.setText("Dejar vacío para no cambiarla");
        btnEstado.setText(seleccionado.isEstado() ? "Desactivar" : "Activar");
        btnEstado.setEnabled(true);
        lblModo.setText("Editando: " + seleccionado.getUsuario());
    }

    private void seleccionarRol(int idRol) {
        for (int i = 0; i < cboRol.getItemCount(); i++) {
            if (cboRol.getItemAt(i).getIdRol() == idRol) {
                cboRol.setSelectedIndex(i);
                return;
            }
        }
    }

    // ================================================================== acciones

    private void guardar() {
        Usuario u = leerFormulario();
        String pass = new String(txtPassword.getPassword());
        String conf = new String(txtConfirmar.getPassword());
        try {
            if (seleccionado == null) {
                controller.registrar(u, pass, conf);
                info("Usuario registrado correctamente");
            } else {
                controller.modificar(u, pass, conf);
                info("Usuario modificado correctamente");
            }
            cargarUsuarios();
            modoNuevo();
        } catch (NegocioException ex) {
            error(ex.getMessage());
        } catch (Exception ex) {
            error("Error al guardar el usuario:\n" + ex.getMessage());
        } finally {
            txtPassword.setText("");
            txtConfirmar.setText("");
        }
    }

    private void cambiarEstado() {
        if (seleccionado == null) {
            error("Seleccione un usuario de la lista");
            return;
        }
        boolean activar = !seleccionado.isEstado();
        String accion = activar ? "activar" : "desactivar";
        String detalle = activar ? "Podrá volver a iniciar sesión." : "Ya no podrá iniciar sesión.";
        if (!confirmar("¿Desea " + accion + " la cuenta \"" + seleccionado.getUsuario() + "\"?\n" + detalle)) {
            return;
        }
        try {
            controller.cambiarEstado(seleccionado.getIdUsuario(), activar);
            info("Cuenta " + (activar ? "activada" : "desactivada"));
            cargarUsuarios();
            modoNuevo();
        } catch (NegocioException ex) {
            error(ex.getMessage());
        } catch (Exception ex) {
            error("No se pudo cambiar el estado:\n" + ex.getMessage());
        }
    }

    private Usuario leerFormulario() {
        Usuario u = new Usuario();
        if (seleccionado != null) {
            u.setIdUsuario(seleccionado.getIdUsuario());
            u.setEstado(seleccionado.isEstado());
        }
        u.setNombres(txtNombres.getText());
        u.setApellidos(txtApellidos.getText());
        u.setDni(txtDni.getText());
        u.setEmail(txtEmail.getText());
        u.setUsuario(txtUsuario.getText());
        Rol rol = (Rol) cboRol.getSelectedItem();
        u.setRol(rol);
        u.setIdRol(rol == null ? 0 : rol.getIdRol());
        return u;
    }

    // ================================================================== render

    /** Pinta en gris las cuentas desactivadas. */
    private class RenderInactivo extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel,
                                                       boolean foco, int fila, int col) {
            Component comp = super.getTableCellRendererComponent(t, valor, sel, foco, fila, col);
            if (!sel) {
                boolean inactivo = fila < usuariosEnTabla.size() && !usuariosEnTabla.get(fila).isEstado();
                comp.setBackground(inactivo ? GRIS : t.getBackground());
            }
            return comp;
        }
    }
}
