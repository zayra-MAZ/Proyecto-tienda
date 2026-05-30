package Tienda;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VistaGerente extends JFrame {

    private static final Color ROSA_FUERTE  = new Color(174, 42, 126);
    private static final Color AZUL_HEADER  = new Color(36, 36, 80);
    private static final Color FONDO        = new Color(245, 247, 250);

    private DefaultTableModel modeloTabla;
    private JTable tablaProductos;

    private DefaultTableModel modeloEmpleados;
    private JTable tablaEmpleados;

    public VistaGerente(String nombreGerente) {

        setTitle("Panel de Gerente — " + nombreGerente);
        setSize(900, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AZUL_HEADER);
        header.setPreferredSize(new Dimension(0, 55));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitulo = new JLabel("⚙  Panel de Gerente");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        JLabel lblUser = new JLabel("👤 " + nombreGerente);
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblUser.setForeground(new Color(180, 180, 220));
        header.add(lblUser, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));
        pestanas.addTab("🛒  Productos",   panelProductos());
        pestanas.addTab("🚴  Domicilios",  panelDomicilios());
        add(pestanas, BorderLayout.CENTER);

        cargarProductosDesdeDB();
        cargarEmpleados();
    }

    private JPanel panelProductos() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Categoría", "Precio ($)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                return c == 1 || c == 2 || c == 3; 
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(26);
        tablaProductos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaProductos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaProductos.getTableHeader().setBackground(ROSA_FUERTE);
        tablaProductos.getTableHeader().setForeground(Color.WHITE);
        tablaProductos.setSelectionBackground(new Color(231, 82, 157, 60));
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(50);
        tablaProductos.getColumnModel().getColumn(3).setMaxWidth(100);

        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JPanel formAgregar = new JPanel(new GridBagLayout());
        formAgregar.setBackground(FONDO);
        formAgregar.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ROSA_FUERTE),
            "➕  Agregar nuevo producto",
            0, 0,
            new Font("SansSerif", Font.BOLD, 12),
            ROSA_FUERTE
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre    = new JTextField(14);
        JTextField txtCategoria = new JTextField(10);
        JTextField txtPrecio    = new JTextField(7);

        final String[] rutaImagenFinal = {""};

        JLabel lblPreview = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(58, 58));
        lblPreview.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        lblPreview.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblPreview.setForeground(new Color(150, 150, 150));

        JLabel lblNombreArchivo = new JLabel("Ninguna imagen seleccionada");
        lblNombreArchivo.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblNombreArchivo.setForeground(new Color(120, 120, 120));

        JButton btnSeleccionarImg = boton("📁 Seleccionar imagen", new Color(52, 152, 219));
        btnSeleccionarImg.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Selecciona la imagen del producto");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (PNG, JPG, GIF)", "png", "jpg", "jpeg", "gif"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                java.io.File origen = fc.getSelectedFile();
                rutaImagenFinal[0] = origen.getAbsolutePath();
                lblNombreArchivo.setText("✅ " + origen.getName());
                lblNombreArchivo.setForeground(new Color(39, 174, 96));
                Image img = new ImageIcon(origen.getAbsolutePath())
                    .getImage().getScaledInstance(56, 56, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(img));
                lblPreview.setText("");
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; formAgregar.add(new JLabel("Nombre:"),    gbc);
        gbc.gridx = 1;               formAgregar.add(txtNombre,                 gbc);
        gbc.gridx = 2;               formAgregar.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 3;               formAgregar.add(txtCategoria,              gbc);
        gbc.gridx = 4;               formAgregar.add(new JLabel("Precio:"),     gbc);
        gbc.gridx = 5;               formAgregar.add(txtPrecio,                 gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; formAgregar.add(new JLabel("Imagen:"),    gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; formAgregar.add(btnSeleccionarImg,    gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 3; gbc.gridwidth = 2; formAgregar.add(lblNombreArchivo,     gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 5; formAgregar.add(lblPreview, gbc);

        JButton btnAgregar = boton("Agregar", new Color(46, 204, 113));
        gbc.gridx = 5; gbc.gridy = 2;
        formAgregar.add(btnAgregar, gbc);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        acciones.setBackground(FONDO);

        JButton btnGuardarPrecio = boton("💾  Guardar cambios en tabla", ROSA_FUERTE);
        JButton btnEliminar      = boton("🗑  Eliminar seleccionado",    new Color(231, 76, 60));

        acciones.add(btnGuardarPrecio);
        acciones.add(btnEliminar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(FONDO);
        sur.add(formAgregar, BorderLayout.NORTH);
        sur.add(acciones,    BorderLayout.SOUTH);
        panel.add(sur, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String cat    = txtCategoria.getText().trim();
            String ruta   = rutaImagenFinal[0];
            double precio;
            try {
                precio = Double.parseDouble(txtPrecio.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número.");
                return;
            }
            if (nombre.isEmpty() || cat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y categoría son obligatorios.");
                return;
            }
            agregarProductoDB(nombre, cat, precio, ruta);
            txtNombre.setText(""); txtCategoria.setText("");
            txtPrecio.setText("");
            rutaImagenFinal[0] = "";
            lblNombreArchivo.setText("Ninguna imagen seleccionada");
            lblNombreArchivo.setForeground(new Color(120, 120, 120));
            lblPreview.setIcon(null);
            lblPreview.setText("Sin imagen");
            cargarProductosDesdeDB();
        });

        btnGuardarPrecio.addActionListener(e -> {
            if (tablaProductos.isEditing()) tablaProductos.getCellEditor().stopCellEditing();

            int filas = modeloTabla.getRowCount();
            int actualizados = 0;
            for (int i = 0; i < filas; i++) {
                try {
                    int id       = (int) modeloTabla.getValueAt(i, 0);
                    String nom   = modeloTabla.getValueAt(i, 1).toString().trim();
                    String cat   = modeloTabla.getValueAt(i, 2).toString().trim();
                    double prec  = Double.parseDouble(
                        modeloTabla.getValueAt(i, 3).toString().replace(",", "."));
                    actualizarProductoDB(id, nom, cat, prec);
                    actualizados++;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error en fila " + (i+1) + ": " + ex.getMessage());
                }
            }
            JOptionPane.showMessageDialog(this,
                actualizados + " producto(s) actualizados correctamente.");
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            String nombre = modeloTabla.getValueAt(fila, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + nombre + "\"?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eliminarProductoDB(id);
                cargarProductosDesdeDB();
            }
        });

        return panel;
    }

    private JPanel panelDomicilios() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        modeloEmpleados = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Teléfono", "Estado"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaEmpleados = new JTable(modeloEmpleados);
        tablaEmpleados.setRowHeight(26);
        tablaEmpleados.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaEmpleados.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaEmpleados.getTableHeader().setBackground(new Color(36, 36, 80));
        tablaEmpleados.getTableHeader().setForeground(Color.WHITE);
        tablaEmpleados.setSelectionBackground(new Color(52, 152, 219, 70));
        tablaEmpleados.getColumnModel().getColumn(0).setMaxWidth(50);

        tablaEmpleados.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String estado = modeloEmpleados.getValueAt(row, 3).toString();
                if (!sel) {
                    c.setBackground("Disponible".equals(estado)
                        ? new Color(220, 250, 220)
                        : new Color(255, 220, 220));
                }
                return c;
            }
        });

        panel.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        JPanel formDom = new JPanel(new GridBagLayout());
        formDom.setBackground(FONDO);
        formDom.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(36, 36, 80)),
            "🚴  Asignar domicilio",
            0, 0,
            new Font("SansSerif", Font.BOLD, 12),
            new Color(36, 36, 80)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField txtDireccion = new JTextField(22);
        JTextField txtNotas     = new JTextField(22);
        JButton    btnRefrescar = boton("🔄  Refrescar", new Color(52, 152, 219));
        JButton    btnAsignar   = boton("📤  Asignar", ROSA_FUERTE);
        JButton    btnLiberar   = boton("✅  Marcar entregado", new Color(39, 174, 96));

        gbc.gridx = 0; gbc.gridy = 0;
        formDom.add(new JLabel("Dirección de entrega:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formDom.add(txtDireccion, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        formDom.add(new JLabel("Notas (opcional):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formDom.add(txtNotas, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; formDom.add(btnRefrescar, gbc);
        gbc.gridx = 1;               formDom.add(btnAsignar,   gbc);
        gbc.gridx = 2;               formDom.add(btnLiberar,   gbc);

        panel.add(formDom, BorderLayout.SOUTH);

        btnRefrescar.addActionListener(e -> cargarEmpleados());

        btnAsignar.addActionListener(e -> {
            int fila = tablaEmpleados.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un empleado.");
                return;
            }
            String estado = modeloEmpleados.getValueAt(fila, 3).toString();
            if (!"Disponible".equals(estado)) {
                JOptionPane.showMessageDialog(this,
                    "El empleado ya está en una entrega.");
                return;
            }
            String dir = txtDireccion.getText().trim();
            if (dir.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa la dirección.");
                return;
            }
            int empleadoId = (int) modeloEmpleados.getValueAt(fila, 0);
            String notas   = txtNotas.getText().trim();
            asignarDomicilio(empleadoId, dir, notas);
            txtDireccion.setText(""); txtNotas.setText("");
            cargarEmpleados();
        });

        btnLiberar.addActionListener(e -> {
            int fila = tablaEmpleados.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un empleado.");
                return;
            }
            int empleadoId = (int) modeloEmpleados.getValueAt(fila, 0);
            liberarEmpleado(empleadoId);
            cargarEmpleados();
        });

        return panel;
    }


    private void cargarProductosDesdeDB() {
        modeloTabla.setRowCount(0);
        String sql = "SELECT id, nombre, categoria, precio FROM productos ORDER BY categoria, nombre";
        try (Connection con = Conexion.conectar();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage());
        }
    }

    private void agregarProductoDB(String nombre, String categoria,
                                   double precio, String ruta) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, ruta_imagen) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            pst.setString(2, categoria);
            pst.setDouble(3, precio);
            pst.setString(4, ruta);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Producto \"" + nombre + "\" agregado.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizarProductoDB(int id, String nombre,
                                      String categoria, double precio) {
        String sql = "UPDATE productos SET nombre=?, categoria=?, precio=? WHERE id=?";
        try (Connection con = Conexion.conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            pst.setString(2, categoria);
            pst.setDouble(3, precio);
            pst.setInt(4, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void eliminarProductoDB(int id) {
        try (Connection con = Conexion.conectar();
             PreparedStatement pst = con.prepareStatement(
                 "DELETE FROM productos WHERE id=?")) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    private void cargarEmpleados() {
        modeloEmpleados.setRowCount(0);
        String sql = "SELECT id, nombre, telefono, disponible FROM empleados ORDER BY nombre";
        try (Connection con = Conexion.conectar();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String estado = rs.getInt("disponible") == 1 ? "Disponible" : "En entrega";
                modeloEmpleados.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    estado
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + ex.getMessage());
        }
    }

    private void asignarDomicilio(int empleadoId, String direccion, String notas) {
        try (Connection con = Conexion.conectar()) {
            String sqlDom = "INSERT INTO domicilios (empleado_id, direccion, notas) " +
                            "VALUES (?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sqlDom)) {
                pst.setInt(1, empleadoId);
                pst.setString(2, direccion);
                pst.setString(3, notas);
                pst.executeUpdate();
            }
            
            try (PreparedStatement pst2 = con.prepareStatement(
                    "UPDATE empleados SET disponible=0 WHERE id=?")) {
                pst2.setInt(1, empleadoId);
                pst2.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Domicilio asignado correctamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void liberarEmpleado(int empleadoId) { //hay un errorcito de que primero lo teniamos como ventana empleado y al final lo cambiamos por cliente, pero solo lo hicimos de vista
        try (Connection con = Conexion.conectar()) {
            try (PreparedStatement pst = con.prepareStatement(
                    "UPDATE domicilios SET estado='entregado' " +
                    "WHERE empleado_id=? AND estado='pendiente'")) {
                pst.setInt(1, empleadoId);
                pst.executeUpdate();
            }
            try (PreparedStatement pst2 = con.prepareStatement(
                    "UPDATE empleados SET disponible=1 WHERE id=?")) {
                pst2.setInt(1, empleadoId);
                pst2.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Empleado marcado como disponible.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return b;
    }
}
