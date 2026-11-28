package com.ferrered.capa.presentacion;

import com.ferrered.capa.datos.entidades.Categoria;
import com.ferrered.capa.datos.entidades.Producto;
import com.ferrered.capa.negocio.ProductoController;
import com.ferrered.capa.negocio.util.NegocioException;
import com.ferrered.capa.negocio.util.Validador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * CUS-02 Gestionar Productos. Responsable: Módulo A - Seguridad y Catálogo.
 *
 * Uso:
 *  - "Nuevo": limpia el formulario para registrar un producto.
 *  - Clic en una fila: carga el producto en el formulario para editarlo.
 *  - "Guardar": registra (si es nuevo) o modifica (si se seleccionó uno).
 *  - "Desactivar": eliminación lógica del producto seleccionado.
 * Las filas en rojo tienen stock actual <= stock mínimo.
 *
 * Esta clase solo se comunica con ProductoController (patrón MVC / Capas).
 */
public class FrmProductos extends FrmBase {

    private static final Color ROJO_SUAVE = new Color(255, 220, 220);

    private final ProductoController controller = new ProductoController();

    /** Productos mostrados actualmente en la tabla (misma posición que las filas). */
    private List<Producto> productosEnTabla = new ArrayList<>();
    /** 0 = registrando uno nuevo; > 0 = editando ese producto. */
    private int idSeleccionado = 0;

    // ---- búsqueda ----
    private final JTextField txtBuscar = new JTextField(22);
    private final JComboBox<Categoria> cboFiltroCategoria = new JComboBox<>();
    private final JButton btnBuscar = new JButton("Buscar");

    // ---- tabla ----
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Código", "Nombre", "Categoría", "P. Compra", "P. Venta", "Stock", "Stock mín."}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    // ---- formulario ----
    private final JLabel lblModo = new JLabel();
    private final JTextField txtCodigo = new JTextField(16);
    private final JTextField txtNombre = new JTextField(16);
    private final JTextField txtDescripcion = new JTextField(16);
    private final JComboBox<Categoria> cboCategoria = new JComboBox<>();
    private final JTextField txtPrecioCompra = new JTextField(16);
    private final JTextField txtPrecioVenta = new JTextField(16);
    private final JTextField txtStockInicial = new JTextField(16);
    private final JTextField txtStockMinimo = new JTextField(16);

    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnDesactivar = new JButton("Desactivar");

    public FrmProductos() {
        super("Gestión de Productos");
        setSize(1150, 650);
        setLocationRelativeTo(null);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        centro.add(crearPanelBusqueda(), BorderLayout.NORTH);
        centro.add(crearPanelTabla(), BorderLayout.CENTER);
        centro.add(crearPanelFormulario(), BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);

        cargarCategorias();       // antes de los eventos, para no disparar búsquedas al llenar el combo
        registrarEventos();
        cargarTabla();
        modoNuevo();
    }

    // ================================================================== armado de la vista

    private JPanel crearPanelBusqueda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.add(etiqueta("Buscar (nombre o código):", 'B', txtBuscar));
        p.add(txtBuscar);
        p.add(new JLabel("Categoría:"));
        p.add(cboFiltroCategoria);
        p.add(btnBuscar);
        return p;
    }

    private JScrollPane crearPanelTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setDefaultRenderer(Object.class, new RenderStockBajo());
        tabla.getColumnModel().getColumn(1).setPreferredWidth(220);
        return new JScrollPane(tabla);
    }

    private JPanel crearPanelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Datos del producto"));
        p.setPreferredSize(new Dimension(360, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 8, 5, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;
        c.gridx = 0; c.gridy = fila++; c.gridwidth = 2;
        lblModo.setFont(lblModo.getFont().deriveFont(java.awt.Font.BOLD));
        p.add(lblModo, c);
        c.gridwidth = 1;

        fila = agregarCampo(p, c, fila, etiqueta("Código de barras *", 'C', txtCodigo), txtCodigo);
        fila = agregarCampo(p, c, fila, etiqueta("Nombre *", 'N', txtNombre), txtNombre);
        fila = agregarCampo(p, c, fila, etiqueta("Descripción", 'D', txtDescripcion), txtDescripcion);
        fila = agregarCampo(p, c, fila, etiqueta("Categoría *", 'A', cboCategoria), cboCategoria);
        fila = agregarCampo(p, c, fila, etiqueta("Precio compra (S/) *", 'P', txtPrecioCompra), txtPrecioCompra);
        fila = agregarCampo(p, c, fila, etiqueta("Precio venta (S/) *", 'V', txtPrecioVenta), txtPrecioVenta);
        fila = agregarCampo(p, c, fila, etiqueta("Stock inicial *", 'S', txtStockInicial), txtStockInicial);
        fila = agregarCampo(p, c, fila, etiqueta("Stock mínimo *", 'M', txtStockMinimo), txtStockMinimo);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnDesactivar);
        c.gridx = 0; c.gridy = fila++; c.gridwidth = 2;
        c.insets = new Insets(16, 8, 5, 8);
        p.add(botones, c);

        c.gridy = fila;
        c.weighty = 1;                               // empuja todo hacia arriba
        p.add(new JLabel("<html><small>* obligatorio. Filas en rojo: stock bajo.</small></html>"), c);
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
        btnBuscar.addActionListener(e -> cargarTabla());
        txtBuscar.addActionListener(e -> cargarTabla());            // Enter en el buscador
        cboFiltroCategoria.addActionListener(e -> cargarTabla());
        btnNuevo.addActionListener(e -> modoNuevo());
        btnGuardar.addActionListener(e -> guardar());
        btnDesactivar.addActionListener(e -> desactivar());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionado();
            }
        });
    }

    // ================================================================== carga de datos

    private void cargarCategorias() {
        try {
            List<Categoria> categorias = controller.listarCategorias();
            Categoria todas = new Categoria();
            todas.setIdCategoria(0);
            todas.setNombre("Todas");
            cboFiltroCategoria.addItem(todas);
            for (Categoria cat : categorias) {
                cboFiltroCategoria.addItem(cat);
                cboCategoria.addItem(cat);
            }
        } catch (Exception ex) {
            error("No se pudieron cargar las categorías:\n" + ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            Categoria filtro = (Categoria) cboFiltroCategoria.getSelectedItem();
            Integer idCat = (filtro == null) ? null : filtro.getIdCategoria();
            productosEnTabla = controller.buscar(txtBuscar.getText(), idCat);

            modelo.setRowCount(0);
            for (Producto p : productosEnTabla) {
                modelo.addRow(new Object[]{
                        p.getCodigoBarras(), p.getNombre(), p.getNombreCategoria(),
                        p.getPrecioCompra(), p.getPrecioVenta(), p.getStockActual(), p.getStockMinimo()
                });
            }
        } catch (Exception ex) {
            error("No se pudo cargar la lista de productos:\n" + ex.getMessage());
        }
    }

    // ================================================================== modos del formulario

    private void modoNuevo() {
        idSeleccionado = 0;
        tabla.clearSelection();
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        txtStockInicial.setText("0");
        txtStockMinimo.setText("0");
        if (cboCategoria.getItemCount() > 0) {
            cboCategoria.setSelectedIndex(0);
        }
        txtStockInicial.setEnabled(true);
        btnDesactivar.setEnabled(false);
        lblModo.setText("Nuevo producto");
        txtCodigo.requestFocus();
    }

    private void cargarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= productosEnTabla.size()) {
            return;
        }
        Producto p = productosEnTabla.get(fila);
        idSeleccionado = p.getIdProducto();
        txtCodigo.setText(p.getCodigoBarras());
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion() == null ? "" : p.getDescripcion());
        txtPrecioCompra.setText(p.getPrecioCompra().toPlainString());
        txtPrecioVenta.setText(p.getPrecioVenta().toPlainString());
        txtStockInicial.setText(String.valueOf(p.getStockActual()));
        txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
        seleccionarCategoria(p.getIdCategoria());

        // El stock actual solo cambia con compras, ventas y devoluciones.
        txtStockInicial.setEnabled(false);
        btnDesactivar.setEnabled(true);
        lblModo.setText("Editando: " + p.getNombre());
    }

    private void seleccionarCategoria(int idCategoria) {
        for (int i = 0; i < cboCategoria.getItemCount(); i++) {
            if (cboCategoria.getItemAt(i).getIdCategoria() == idCategoria) {
                cboCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    // ================================================================== acciones

    private void guardar() {
        try {
            Producto p = leerFormulario();
            if (idSeleccionado == 0) {
                controller.registrar(p);
                info("Producto registrado correctamente");
            } else {
                controller.modificar(p);
                info("Producto modificado correctamente");
            }
            cargarTabla();
            modoNuevo();
        } catch (NegocioException ex) {
            error(ex.getMessage());                 // CUS-02 A1 / A2
        } catch (Exception ex) {
            error("Error al guardar el producto:\n" + ex.getMessage());
        }
    }

    private void desactivar() {
        if (idSeleccionado == 0) {
            error("Seleccione un producto de la lista");
            return;
        }
        if (!confirmar("¿Desactivar el producto \"" + txtNombre.getText() + "\"?\n"
                + "Ya no aparecerá en ventas ni en la lista, pero se conserva su historial.")) {
            return;
        }
        try {
            controller.desactivar(idSeleccionado);
            info("Producto desactivado");
            cargarTabla();
            modoNuevo();
        } catch (Exception ex) {
            error("No se pudo desactivar:\n" + ex.getMessage());
        }
    }

    /** Arma el Producto con lo escrito. Los errores de formato llegan como NegocioException. */
    private Producto leerFormulario() throws NegocioException {
        Producto p = new Producto();
        p.setIdProducto(idSeleccionado);
        p.setCodigoBarras(txtCodigo.getText().trim());
        p.setNombre(txtNombre.getText().trim());
        String desc = txtDescripcion.getText().trim();
        p.setDescripcion(desc.isEmpty() ? null : desc);

        Categoria cat = (Categoria) cboCategoria.getSelectedItem();
        p.setIdCategoria(cat == null ? 0 : cat.getIdCategoria());

        p.setPrecioCompra(Validador.aDecimal(txtPrecioCompra.getText(), "Precio compra"));
        p.setPrecioVenta(Validador.aDecimal(txtPrecioVenta.getText(), "Precio venta"));
        p.setStockActual(Validador.aEntero(txtStockInicial.getText(), "Stock inicial"));
        p.setStockMinimo(Validador.aEntero(txtStockMinimo.getText(), "Stock mínimo"));
        return p;
    }

    // ================================================================== render

    /** Pinta en rojo suave las filas con stock actual <= stock mínimo. */
    private class RenderStockBajo extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionado,
                                                       boolean foco, int fila, int col) {
            Component comp = super.getTableCellRendererComponent(t, valor, seleccionado, foco, fila, col);
            if (!seleccionado) {
                boolean bajo = fila < productosEnTabla.size() && productosEnTabla.get(fila).stockBajo();
                comp.setBackground(bajo ? ROJO_SUAVE : t.getBackground());
            }
            return comp;
        }
    }
}
