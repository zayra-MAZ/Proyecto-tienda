package Tienda;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

public class vista extends JFrame {

private JPanel listaItemsPanel; 
private JLabel valSub;          
private JLabel valTotal;        
    
    private JTextField buscador;
    private JButton carritoDeCompras;
    private JButton mostrarProductos;
    private JTabbedPane pestañasComponente; 
    private JPanel aquiProductos;
    private JPanel aquiLasCategorias;
    private JPanel contenedorCentral;
    private JPanel panelLateralCarrito;
    private boolean carritoVisible = false;
    private List<ItemCarritoTmp> itemsDelCarrito = new ArrayList<>();
    private List<JButton> botonesCarritoTarjetas = new ArrayList<>();
    private final Map<String, JButton> botonesDeCategorias = new LinkedHashMap<>();
    private String categoriasSelecc = "Todos";;
    private JLabel lblTotalProductos;
    private JLabel lblMontoTotal;
    private final Color colorRosaFuerte = new Color(174, 42, 126);
    private final Color colorRosa = new Color(231, 82, 157);
    private final Color rosaSuave = new Color(255, 235, 246);
    private final Color verdeFondo = new Color(239, 250, 233);
    private final Color verdeBoton = new Color(204, 230, 196);
    private final Color verdeHover = new Color(181, 216, 173);
    private final Color textoOscuro = new Color(45, 45, 55);
    private final Color textoGris = new Color(130, 130, 140);

    private final DecimalFormat formatoPrecio = new DecimalFormat("$#,##0.00");

    private String nombreUsuario = "Usuario";

    public vista(List<String> categorias) {
        this(categorias, "Usuario");
    }

    public vista(List<String> categorias, String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        aplicarTema();

        setTitle("Tienda Error 143");
        setSize(1150, 750); 
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        pestañasComponente = new JTabbedPane(); 

        crearEncabezado();
        crearCategorias(categorias);
        carritoVisible = true; 

        crearCentro();
        seleccionaCategoria("Todos");
        configurarAccionesLocales();
    }

    private void configurarAccionesLocales() {
       
        carritoDeCompras.addActionListener(e -> conmutarCarrito());
    }
    public void conmutarCarrito() {
        carritoVisible = !carritoVisible;
        if (carritoVisible) {
            contenedorCentral.add(panelLateralCarrito, BorderLayout.EAST);
        } else {
            contenedorCentral.remove(panelLateralCarrito);
        }
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }

    public JTabbedPane getPestañas() {
        return pestañasComponente; 
    }

    public List<JButton> getBotonesCarritoTarjetas() {
        return botonesCarritoTarjetas;
    }

    private void aplicarTema() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 999);
            UIManager.put("Component.arc", 24);
            UIManager.put("TextComponent.arc", 999);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));

            IconFontSwing.register(FontAwesome.getIconFont());
        } catch (Exception e) {
            System.out.println("No se pudo cargar FlatLaf o FontAwesome.");
        }
    }

    private Icon crearIcono(FontAwesome icono, int size, Color color) {
        return IconFontSwing.buildIcon(icono, size, color);
    }

    private void crearEncabezado() {
        GradientPanel encabezado = new GradientPanel(
                new Color(153, 36, 120),
                new Color(255, 118, 181)
        );
        encabezado.setPreferredSize(new Dimension(1000, 92));
        encabezado.setLayout(new MigLayout("fill, insets 14 24 14 24", "[190!][grow,fill][210!]", "[grow,fill]"));

        JPanel marca = new JPanel(new MigLayout("insets 0, gap 0", "[grow]", "[]0[]"));
        marca.setOpaque(false);

        JLabel titulo = new JLabel("Error 143");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel subtitulo = new JLabel("Mini Market");
        subtitulo.setForeground(new Color(255, 235, 248));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        marca.add(titulo, "wrap");
        marca.add(subtitulo);

        JPanel panelBuscador = crearBuscador();

        JPanel derecha = new JPanel(new MigLayout("insets 0, gap 12", "[][]", "[center]"));
        derecha.setOpaque(false);

        carritoDeCompras = new BotonRedondo("", Color.WHITE, rosaSuave, Color.WHITE, colorRosaFuerte);
        carritoDeCompras.setIcon(crearIcono(FontAwesome.SHOPPING_CART, 23, colorRosaFuerte));
        carritoDeCompras.setPreferredSize(new Dimension(58, 46));

        JLabel usuario = new JLabel(nombreUsuario);
        usuario.setIcon(crearIcono(FontAwesome.USER, 15, Color.WHITE));
        usuario.setIconTextGap(8);
        usuario.setForeground(Color.WHITE);
        usuario.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JButton btnCerrarSesion = new BotonRedondo("Salir", new Color(220, 53, 69), new Color(200, 35, 51), Color.WHITE, Color.WHITE);
        btnCerrarSesion.setIcon(crearIcono(FontAwesome.SIGN_OUT, 15, Color.WHITE));
        btnCerrarSesion.setIconTextGap(6);
        btnCerrarSesion.setPreferredSize(new Dimension(90, 38));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.addActionListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this, "¿Cerrar sesión?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                dispose();
                javax.swing.SwingUtilities.invokeLater(() -> new Login().setVisible(true));
            }
        });

        derecha.add(carritoDeCompras, "w 58!, h 46!");
        derecha.add(usuario);
        derecha.add(btnCerrarSesion, "w 90!, h 38!");

        encabezado.add(marca, "cell 0 0, grow");
        encabezado.add(panelBuscador, "cell 1 0, growx, h 50!");
        encabezado.add(derecha, "cell 2 0, align right center");

        add(encabezado, BorderLayout.NORTH);
    }

    private JPanel crearBuscador() {
        PanelRedondo contenedor = new PanelRedondo(Color.WHITE, 30);
        contenedor.setLayout(new MigLayout("fill, insets 0 18 0 16", "[][grow]", "[center]"));

        JLabel icono = new JLabel();
        icono.setIcon(crearIcono(FontAwesome.SEARCH, 17, textoOscuro));

        buscador = new JTextField();
        buscador.setBorder(null);
        buscador.setOpaque(false);
        buscador.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        buscador.setForeground(textoOscuro);

        contenedor.add(icono, "w 28!");
        contenedor.add(buscador, "growx");

        return contenedor;
    }

    private void crearCategorias(List<String> categorias) {
        aquiLasCategorias = new JPanel(new MigLayout("fillx, insets 22 16 22 16", "[grow,fill]", "[]4[]18[]"));
        aquiLasCategorias.setBackground(verdeFondo);
        aquiLasCategorias.setPreferredSize(new Dimension(220, 700));

        JLabel titulo = new JLabel("Categorías");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titulo.setForeground(new Color(58, 82, 58));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descripcion = new JLabel("Elige una sección");
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descripcion.setForeground(new Color(100, 125, 100));
        descripcion.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel contenedorBotones = new JPanel(new MigLayout("fillx, insets 0, gapy 11", "[grow,fill]", ""));
        contenedorBotones.setOpaque(false);

        mostrarProductos = crearBotonCategoria("Todos", FontAwesome.HOME);
        botonesDeCategorias.put("Todos", mostrarProductos);
        contenedorBotones.add(mostrarProductos, "h 58!, wrap");

        for (String categoria : categorias) {
            if (categoria.equalsIgnoreCase("Todos")) {
                continue;
            }

            JButton boton = crearBotonCategoria(categoria, obtenerIconoCategoria(categoria));
            botonesDeCategorias.put(categoria, boton);
            contenedorBotones.add(boton, "h 58!, wrap");
        }

        aquiLasCategorias.add(titulo, "wrap");
        aquiLasCategorias.add(descripcion, "wrap");
        aquiLasCategorias.add(contenedorBotones, "growx, pushy, aligny top");

        add(aquiLasCategorias, BorderLayout.WEST);
    }

    private JButton crearBotonCategoria(String texto, FontAwesome icono) {
        BotonRedondo boton = new BotonRedondo(texto, verdeBoton, verdeHover, colorRosa, new Color(45, 75, 45));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setIconTextGap(14);
        boton.setBorder(new EmptyBorder(0, 18, 0, 12));
        boton.setIcon(crearIcono(icono, 23, new Color(45, 75, 45)));
        boton.putClientProperty("iconoCategoria", icono);

        return boton;
    }

    private FontAwesome obtenerIconoCategoria(String categoria) {
        String c = categoria.toLowerCase();
        if (c.contains("limpieza")) return FontAwesome.TINT;
        if (c.contains("alimento") || c.contains("comida")) return FontAwesome.CUTLERY;
        if (c.contains("golosina") || c.contains("dulce")) return FontAwesome.GIFT;
        if (c.contains("bebida")) return FontAwesome.GLASS;
        if (c.contains("higiene")) return FontAwesome.LEAF;
        return FontAwesome.TAG;
    }

   private void crearCentro() {
        contenedorCentral = new JPanel(new BorderLayout());
        contenedorCentral.setBackground(Color.WHITE);

        JPanel centroCatalogo = new JPanel(new BorderLayout(0, 15));
        centroCatalogo.setBackground(Color.WHITE);
        centroCatalogo.setBorder(new EmptyBorder(20, 24, 20, 10));

        centroCatalogo.add(crearBanner(), BorderLayout.NORTH);

        aquiProductos = new JPanel(new MigLayout("wrap 4, insets 8 0 20 0, gap 14 20", "[168!][168!][168!][168!]", ""));
        aquiProductos.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(aquiProductos);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        pestañasComponente.addTab("Tienda", scroll);

        pestañasComponente.addTab("Pasarela de Pago", crearPanelPago());
        

        centroCatalogo.add(pestañasComponente, BorderLayout.CENTER);
        
        
        crearPanelLateralCarrito();

        contenedorCentral.add(centroCatalogo, BorderLayout.CENTER);
        
        contenedorCentral.add(panelLateralCarrito, BorderLayout.EAST); 

        add(contenedorCentral, BorderLayout.CENTER);
    }
    private class ItemCarritoTmp {
    String rutaImg;
    String nombre;
    double precio;
    int cantidad;

     public ItemCarritoTmp(String rutaImg, String nombre, double precio, int cantidad) {
        this.rutaImg = rutaImg;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
      }
    }
    private void crearPanelLateralCarrito() {
        panelLateralCarrito = new JPanel(new MigLayout("wrap, fillx, insets 20 16 20 16", "[grow,fill]", "[][grow][]"));
        panelLateralCarrito.setBackground(new Color(254, 244, 250)); 
        panelLateralCarrito.setPreferredSize(new Dimension(310, 700));
        panelLateralCarrito.setBorder(new EmptyBorder(0, 4, 0, 0));
        {
           
        }
        JPanel headerCarrito = new JPanel(new MigLayout("insets 0, gap 10", "[][grow]", "[center]"));
        headerCarrito.setOpaque(false);
        
        JLabel icCarrito = new JLabel(crearIcono(FontAwesome.SHOPPING_CART, 22, colorRosaFuerte));
        JLabel lbTitulo = new JLabel("Mi carrito (" + itemsDelCarrito.size() + ")");
        lbTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitulo.setForeground(colorRosaFuerte);
        
        headerCarrito.add(icCarrito);
        headerCarrito.add(lbTitulo);
        panelLateralCarrito.add(headerCarrito, "wrap, gap bottom 15");
        listaItemsPanel = new JPanel(new MigLayout("wrap, fillx, insets 0, gapy 12", "[grow,fill]"));
        listaItemsPanel.setOpaque(false);

        actualizarListaTarjetas();

        JScrollPane scrollItems = new JScrollPane(listaItemsPanel);
        scrollItems.setBorder(null);
        scrollItems.setOpaque(false);
        scrollItems.getViewport().setOpaque(false);
        scrollItems.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panelLateralCarrito.add(scrollItems, "grow, pushy, wrap");
        JPanel panelTotales = new JPanel(new MigLayout("wrap, fillx, insets 10 4 10 4", "[grow][right]", "[]8[]15[]20[]"));
        panelTotales.setOpaque(false);

        JLabel lbSub = new JLabel("Subtotal");
        lbSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbSub.setForeground(textoOscuro);
        
        valSub = new JLabel("$0.00");
        valSub.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valSub.setForeground(textoOscuro);

        JLabel lbEnvio = new JLabel("Envío");
        lbEnvio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbEnvio.setForeground(textoOscuro);
        JLabel valEnvio = new JLabel("Gratis");
        valEnvio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valEnvio.setForeground(new Color(60, 160, 90)); 

        JLabel lbTotal = new JLabel("Total");
        lbTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTotal.setForeground(colorRosaFuerte);
        
        valTotal = new JLabel("$0.00");
        valTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valTotal.setForeground(colorRosaFuerte);

        BotonRedondo btnPago = new BotonRedondo("Ir al pago", colorRosa, colorRosaFuerte, colorRosaFuerte, Color.WHITE);
        btnPago.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnPago.setIcon(crearIcono(FontAwesome.ARROW_RIGHT, 14, Color.WHITE));
        btnPago.setHorizontalTextPosition(SwingConstants.LEFT);
        btnPago.setIconTextGap(10);
        btnPago.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPago.addActionListener(e -> {
            pestañasComponente.setSelectedIndex(1); 
        });
        

        panelTotales.add(lbSub);
        panelTotales.add(valSub, "cell 1 0");
        panelTotales.add(lbEnvio);
        panelTotales.add(valEnvio, "cell 1 1");
        panelTotales.add(lbTotal, "gap top 8");
        panelTotales.add(valTotal, "cell 1 2, gap top 8");
        panelTotales.add(btnPago, "span 2, growx, h 48!, gap top 10");

        panelLateralCarrito.add(panelTotales, "growx, aligny bottom");
        
        recalcularCostos();
    }
        private JPanel crearTarjetaItemCarritoVisual(ItemCarritoTmp item) {
        PanelRedondo contenedorItem = new PanelRedondo(Color.WHITE, 20);
        contenedorItem.setLayout(new MigLayout("insets 10 10 10 10, gap 10", "[60!][grow,fill]", "[top]"));
        contenedorItem.setPreferredSize(new Dimension(260, 95));

        JLabel lbImg = new JLabel();
        lbImg.setHorizontalAlignment(SwingConstants.CENTER);
        lbImg.setIcon(cargarImagen(item.rutaImg, 50, 50));

        JPanel txtPanel = new JPanel(new MigLayout("wrap, insets 0, gapy 2", "[grow,fill]"));
        txtPanel.setOpaque(false);

        JLabel name = new JLabel(item.nombre);
        name.setFont(new Font("Segoe UI", Font.BOLD, 13));
        name.setForeground(textoOscuro);

        JLabel price = new JLabel(formatoPrecio.format(item.precio));
        price.setFont(new Font("Segoe UI", Font.BOLD, 13));
        price.setForeground(textoGris);

        JPanel bottomControl = new JPanel(new MigLayout("insets 0, gap 6", "[][18!][][grow][right]"));
        bottomControl.setOpaque(false);

        JButton btnMenos = new JButton("-");
        btnMenos.setMargin(new java.awt.Insets(0,0,0,0));
        btnMenos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnMenos.addActionListener(e -> {
            if (item.cantidad > 1) {
                item.cantidad--;
                actualizarListaTarjetas();
                recalcularCostos();
            }
        });
        
        JLabel lbCant = new JLabel(String.valueOf(item.cantidad), SwingConstants.CENTER);
        lbCant.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton btnMas = new JButton("+");
        btnMas.setMargin(new java.awt.Insets(0,0,0,0));
        btnMas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMas.addActionListener(e -> {
            item.cantidad++;
            actualizarListaTarjetas();
            recalcularCostos();
        });

        JButton btnEliminar = new JButton(crearIcono(FontAwesome.TRASH, 14, textoGris));
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
       
        btnEliminar.addActionListener(e -> {
            itemsDelCarrito.remove(item);
            actualizarListaTarjetas();
            recalcularCostos();
        });

        bottomControl.add(btnMenos, "w 22!, h 20!");
        bottomControl.add(lbCant);
        bottomControl.add(btnMas, "w 22!, h 20!");
        bottomControl.add(btnEliminar, "cell 4 0, w 26!, h 20!");

        txtPanel.add(name);
        txtPanel.add(price);
        txtPanel.add(bottomControl, "gap top 4");

        contenedorItem.add(lbImg, "w 55!, h 55!");
        contenedorItem.add(txtPanel, "growx");

        return contenedorItem;
    }
    private void actualizarListaTarjetas() {
        listaItemsPanel.removeAll();
        for (ItemCarritoTmp item : itemsDelCarrito) {
            listaItemsPanel.add(crearTarjetaItemCarritoVisual(item), "w 260!, h 95!");
        }
        listaItemsPanel.revalidate();
        listaItemsPanel.repaint();
    }
  private JPanel crearPanelPago() {
    JPanel panelPago = new JPanel(new MigLayout("wrap 12, fillx, insets 30", "[grow, fill]", "[]20[]"));
    panelPago.setBackground(Color.WHITE);

    JLabel lblTitulo = new JLabel("Finalizar Compra y Pago");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
    lblTitulo.setForeground(new Color(80, 80, 80));
    panelPago.add(lblTitulo, "span 12, gapbottom 10");
    JPanel panelCliente = new JPanel(new MigLayout("wrap 2, fillx, insets 20", "[][grow, fill]", "[]15[]"));
    panelCliente.setBackground(new Color(254, 244, 250)); 
    panelCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(230, 200, 220), 1, true), 
            " Datos del Cliente ", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(150, 70, 110)));

    panelCliente.add(new JLabel("Nombre Completo:"), "gapright 10");
    JTextField txtNombre = new JTextField();
    panelCliente.add(txtNombre, "growx");

    panelCliente.add(new JLabel("Teléfono:"), "gapright 10");
    JTextField txtTelefono = new JTextField();
    panelCliente.add(txtTelefono, "growx");

    panelCliente.add(new JLabel("Dirección de Entrega:"), "gapright 10");
    JTextField txtDireccion = new JTextField();
    panelCliente.add(txtDireccion, "growx");

    panelCliente.add(new JLabel("Método de Pago:"), "gapright 10, gaptop 10");
    javax.swing.JComboBox<String> comboPago = new javax.swing.JComboBox<>(new String[]{"Efectivo", "Tarjeta de Crédito/Débito", "Transferencia"});
    panelCliente.add(comboPago, "growx, gaptop 10");

    comboPago.addItemListener(e -> {
        if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String seleccion = (String) comboPago.getSelectedItem();
            
            if (seleccion.equals("Tarjeta de Crédito/Débito")) {
                if (pestañasComponente.indexOfTab("Pago con Tarjeta") == -1) {
                    pestañasComponente.addTab("Pago con Tarjeta", crearPanelFormularioTarjeta());
                }
                pestañasComponente.setSelectedIndex(pestañasComponente.indexOfTab("Pago con Tarjeta"));
            } else {
                int indexTarjeta = pestañasComponente.indexOfTab("Pago con Tarjeta");
                if (indexTarjeta != -1) {
                    pestañasComponente.remove(indexTarjeta);
                }
            }
        }
    });

    panelPago.add(panelCliente, "span 7, growy");
    JPanel panelResumen = new JPanel(new MigLayout("wrap 1, fillx, insets 20", "[grow, fill]", "[]10[]"));
    panelResumen.setBackground(new Color(248, 249, 250)); 
    panelResumen.setBorder(javax.swing.BorderFactory.createTitledBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true), 
            " Resumen de Orden ", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(100, 100, 100)));

    lblTotalProductos = new JLabel("Productos en carrito: 0");
    lblTotalProductos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    panelResumen.add(lblTotalProductos);

    lblMontoTotal = new JLabel("Total a Pagar: $0.00");
    lblMontoTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblMontoTotal.setForeground(new Color(180, 40, 90)); 
    panelResumen.add(lblMontoTotal, "gaptop 10, gapbottom 20");

    JButton btnPagar = new JButton("Confirmar y Pagar");
    btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 16));
    btnPagar.setBackground(new Color(150, 70, 110)); 
    btnPagar.setForeground(Color.WHITE);
    btnPagar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    
    btnPagar.addActionListener(e -> {
        if(txtNombre.getText().trim().isEmpty() || txtDireccion.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, completa los campos obligatorios (Nombre y Dirección).", "Campos Incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
        } else {
            String metodo = (String) comboPago.getSelectedItem();
            if (metodo.equals("Tarjeta de Crédito/Débito")) {
                pestañasComponente.setSelectedIndex(pestañasComponente.indexOfTab("Pago con Tarjeta"));
            } else if (metodo.equals("Transferencia")) {
                javax.swing.JOptionPane.showMessageDialog(this, "¡Pedido registrado correctamente!\nPor favor, realiza tu transferencia bancaria a la cuenta oficial de la tienda.\n¡Gracias por tu compra, " + txtNombre.getText() + "!", "Pago por Transferencia", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "¡Compra en efectivo registrada con éxito!\nTe esperamos en caja para entregar tus productos.\n¡Gracias por tu compra, " + txtNombre.getText() + "!", "Pago Exitoso", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    });
    
    panelResumen.add(btnPagar, "growx, height 40");
    panelPago.add(panelResumen, "span 5, growy");

    return panelPago;
}
private JPanel crearPanelFormularioTarjeta() {
    JPanel panelTarjeta = new JPanel(new MigLayout("wrap 12, fillx, insets 30", "[grow, fill]", "[]20[]"));
    panelTarjeta.setBackground(Color.WHITE);

    JLabel lblTitulo = new JLabel("Información de la Tarjeta");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
    lblTitulo.setForeground(colorRosaFuerte);
    panelTarjeta.add(lblTitulo, "span 12, gapbottom 15");

    JPanel formBancario = new JPanel(new MigLayout("wrap 4, fillx, insets 25", "[][grow, fill][][grow, fill]", "[]15[]15[]"));
    formBancario.setBackground(new Color(250, 250, 250));
    formBancario.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));

    formBancario.add(new JLabel("Número de Tarjeta:"), "span 1, gapright 5");
    JTextField txtNumTarjeta = new JTextField();
    formBancario.add(txtNumTarjeta, "span 3, growx, gapbottom 10");

    formBancario.add(new JLabel("Nombre del Titular:"), "span 1, gapright 5");
    JTextField txtTitular = new JTextField();
    formBancario.add(txtTitular, "span 3, growx, gapbottom 10");

    formBancario.add(new JLabel("Vencimiento (MM/AA):"), "span 1, gapright 5");
    JTextField txtVence = new JTextField();
    formBancario.add(txtVence, "span 1, width 80!");

    formBancario.add(new JLabel("CVV (Seguridad):"), "span 1, gapleft 20, gapright 5");
    JTextField txtCvv = new JTextField();
    formBancario.add(txtCvv, "span 1, width 60!");

    panelTarjeta.add(formBancario, "span 8, growy");

    JPanel panelAccion = new JPanel(new MigLayout("wrap 1, fillx, insets 20", "[grow, fill]"));
    panelAccion.setBackground(Color.WHITE);

    JButton btnFinalizarTarjeta = new JButton("Procesar Pago Seguro");
    btnFinalizarTarjeta.setFont(new Font("Segoe UI", Font.BOLD, 16));
    btnFinalizarTarjeta.setBackground(new Color(60, 160, 90)); 
    btnFinalizarTarjeta.setForeground(Color.WHITE);
    btnFinalizarTarjeta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    btnFinalizarTarjeta.addActionListener(e -> {
        if(txtNumTarjeta.getText().trim().isEmpty() || txtTitular.getText().trim().isEmpty() || txtCvv.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, introduce los datos de tu tarjeta bancaria.", "Datos Incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "¡Transacción Bancaria Autorizada!\nSu pago ha sido procesado de manera segura.", "Pago Exitoso", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
           
            pestañasComponente.setSelectedIndex(0); 
            int idx = pestañasComponente.indexOfTab("Pago con Tarjeta");
            if(idx != -1) pestañasComponente.remove(idx);
        }
    });

    panelAccion.add(btnFinalizarTarjeta, "growx, height 50, gaptop 20");
    panelTarjeta.add(panelAccion, "span 4, growy");

    return panelTarjeta;
}
  private void recalcularCostos() {
        double subtotal = 0;
        int totalProductos = 0; 

        if (itemsDelCarrito != null) {
            for (ItemCarritoTmp item : itemsDelCarrito) {
                subtotal += item.precio * item.cantidad;
                totalProductos += item.cantidad; 
            }
        }
        
        if (valSub != null) valSub.setText(formatoPrecio.format(subtotal));
        if (valTotal != null) valTotal.setText(formatoPrecio.format(subtotal));
        if (panelLateralCarrito != null && panelLateralCarrito.getComponentCount() > 0 && panelLateralCarrito.getComponent(0) instanceof JPanel) {
            JPanel header = (JPanel) panelLateralCarrito.getComponent(0);
            for (java.awt.Component comp : header.getComponents()) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getText() != null && label.getText().startsWith("Mi carrito")) {
                        label.setText("Mi carrito (" + totalProductos + ")");
                    if (lblTotalProductos != null) lblTotalProductos.setText("Productos en carrito: " + totalProductos);
if (lblMontoTotal != null) lblMontoTotal.setText("Total a Pagar: " + formatoPrecio.format(subtotal));
                    }
                   
                }
            }
        }
    }
   public void agregarProductoAlCarrito(Producto producto) {
        ItemCarritoTmp existente = null;
        for (ItemCarritoTmp item : itemsDelCarrito) {
            if (item.nombre.equals(producto.getNombre())) {
                existente = item; 
                break;
            }
        }

        if (existente != null) {
            existente.cantidad++;
        } else {
            itemsDelCarrito.add(new ItemCarritoTmp(
                producto.getRutaImagen(), 
                producto.getNombre(), 
                producto.getPrecio(), 
                1
            ));
        }
        actualizarListaTarjetas();
        recalcularCostos();
        int totalProductos = 0;
        for (ItemCarritoTmp item : itemsDelCarrito) {
            totalProductos += item.cantidad; 
        }
      
        if (panelLateralCarrito.getComponentCount() > 0 && panelLateralCarrito.getComponent(0) instanceof JPanel) {
            JPanel header = (JPanel) panelLateralCarrito.getComponent(0);
            for (java.awt.Component comp : header.getComponents()) {
                if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Mi carrito")) {
                    ((JLabel) comp).setText("Mi carrito (" + totalProductos + ")");
                }
            }
        }
    }
    private JPanel crearBanner() {
        PanelBanner banner = new PanelBanner();
        banner.setPreferredSize(new Dimension(700, 125));
        banner.setLayout(new MigLayout("fill, insets 20 28 22 28", "[grow][]", "[center]"));

        JLabel texto = new JLabel(
                "<html>"
                + "<span style='font-size:29px;'><b>OFERTAS DEL MES</b></span><br>"
                + "<span style='font-size:13px;'>Productos bonitos, precios accesibles y diseño moderno</span>"
                + "</html>"
        );
        texto.setForeground(new Color(158, 52, 142));
        texto.setFont(new Font("Segoe UI", Font.BOLD, 26));

        PanelRedondo descuento = new PanelRedondo(new Color(255, 255, 255, 180), 28);
        descuento.setLayout(new MigLayout("fill, insets 8 18 8 18", "[center]", "[]0[]"));

        JLabel d1 = new JLabel("-15%");
        d1.setForeground(colorRosaFuerte);
        d1.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel d2 = new JLabel("seleccionados");
        d2.setForeground(new Color(170, 70, 145));
        d2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        descuento.add(d1, "wrap");
        descuento.add(d2);

        banner.add(texto, "growx");
        banner.add(descuento, "w 150!, h 78!");

        return banner;
    }

    public void mostrarproductos(List<Producto> productos) {
    aquiProductos.removeAll();
    botonesCarritoTarjetas.clear(); 

    if (productos == null || productos.isEmpty()) {
        aquiProductos.revalidate();
        aquiProductos.repaint();
        return;
    }

    for (Producto producto : productos) {
        aquiProductos.add(crearTarjetaProducto(producto), "w 168!, h 250!");
    }

    aquiProductos.revalidate();
    aquiProductos.repaint();
}

   private JPanel crearTarjetaProducto(Producto producto) {
        PanelProducto tarjeta = new PanelProducto();
        tarjeta.setLayout(new MigLayout("wrap, align center, insets 12", "[center]", "[]5[]5[]10[]"));
        JLabel labelImagen = new JLabel();
        labelImagen.setIcon(cargarImagen(producto.getRutaImagen(), 110, 110)); 
        tarjeta.add(labelImagen);
        JLabel nombre = new JLabel(producto.getNombre());
        nombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nombre.setForeground(textoOscuro);
        tarjeta.add(nombre);
        JLabel precio = new JLabel(formatoPrecio.format(producto.getPrecio()));
        precio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        precio.setForeground(colorRosaFuerte); 
        tarjeta.add(precio);

        JPanel panelBotones = new JPanel(new MigLayout("insets 0", "[]15[]")); 
        panelBotones.setOpaque(false);
        JButton btnFavorito = new JButton(IconFontSwing.buildIcon(FontAwesome.HEART, 18, Color.GRAY));
        btnFavorito.setContentAreaFilled(false);
        btnFavorito.setBorderPainted(false);
        btnFavorito.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnFavorito); 
        JButton btnCarrito = new JButton(IconFontSwing.buildIcon(FontAwesome.SHOPPING_CART, 15, new Color(51, 51, 51)));
        btnCarrito.setBackground(new Color(223, 243, 253));
        btnCarrito.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCarrito.setPreferredSize(new Dimension(32, 32)); 
        btnCarrito.setBorder(new EmptyBorder(0, 0, 0, 0));
        btnCarrito.putClientProperty("productoAsociado", producto);
        botonesCarritoTarjetas.add(btnCarrito);                  
        
        panelBotones.add(btnCarrito, "w 32!, h 32!");
        tarjeta.add(panelBotones);

        return tarjeta;
    }

    private ImageIcon cargarImagen(String ruta, int ancho, int alto) {
    if (ruta == null || ruta.isBlank()) {
        System.err.println("Ruta vacía o nula.");
        return null;
    }

    try {
        ruta = ruta.trim().replace("\\", "/");

        // Si viene como /Tienda/imagenes/archivo.png, lo convierte a /imagenes/archivo.png
        if (ruta.startsWith("/Tienda/imagenes/")) {
            ruta = ruta.replace("/Tienda/imagenes/", "/imagenes/");
        }

        // Si viene como /resources/imagenes/archivo.png, lo convierte a /imagenes/archivo.png
        if (ruta.startsWith("/resources/imagenes/")) {
            ruta = ruta.replace("/resources/imagenes/", "/imagenes/");
        }

        // Si viene solo como archivo.png, le agrega /imagenes/
        if (!ruta.startsWith("/") && !ruta.contains(":")) {
            ruta = "/imagenes/" + ruta;
        }

        URL url = getClass().getResource(ruta);

        if (url != null) {
            Image img = new ImageIcon(url).getImage();
            Image imagenEscalada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenEscalada);
        }

        // Intento extra como archivo normal
        java.io.File archivo = new java.io.File(ruta);
        if (archivo.exists()) {
            Image img = new ImageIcon(archivo.getAbsolutePath()).getImage();
            Image imagenEscalada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenEscalada);
        }

        System.err.println("Imagen no encontrada: " + ruta);
        return null;

    } catch (Exception e) {
        System.err.println("Error cargando imagen: " + ruta + " -> " + e.getMessage());
        return null;
    }
}

    public void seleccionaCategoria(String categoria) {
        categoriasSelecc = categoria;

        for (Map.Entry<String, JButton> entrada : botonesDeCategorias.entrySet()) {
            boolean seleccionado = entrada.getKey().equalsIgnoreCase(categoria);
            marcarBoton(entrada.getValue(), seleccionado);
        }
    }

    private void marcarBoton(JButton boton, boolean seleccionado) {
        boton.putClientProperty("seleccionado", seleccionado);

        Color colorTexto = seleccionado ? Color.WHITE : new Color(45, 75, 45);
        boton.setForeground(colorTexto);

        Object iconoGuardado = boton.getClientProperty("iconoCategoria");
        if (iconoGuardado instanceof FontAwesome) {
            boton.setIcon(crearIcono((FontAwesome) iconoGuardado, 23, colorTexto));
        }

        boton.repaint();
    }

    private static Color aclarar(Color color, int cantidad) {
        int r = Math.min(255, color.getRed() + cantidad);
        int g = Math.min(255, color.getGreen() + cantidad);
        int b = Math.min(255, color.getBlue() + cantidad);
        return new Color(r, g, b);
    }

    public String getTextoBuscador() {
        return buscador.getText().trim();
    }

    public String getCategoriasSelecc() {
        return categoriasSelecc;
    }

    public JTextField getBuscador() {
        return buscador;
    }

    public JButton getCarritoDeCompras() {
        return carritoDeCompras;
    }

    public JButton getMostrarProductos() {
        return mostrarProductos;
    }

    public Map<String, JButton> getBotonesDeCategorias() {
        return botonesDeCategorias;
    }

    private class BotonRedondo extends JButton {
        private final Color normal;
        private final Color hover;
        private final Color seleccionado;

        public BotonRedondo(String texto, Color normal, Color hover, Color seleccionado, Color colorTexto) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            this.seleccionado = seleccionado;

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setForeground(colorTexto);
            setBorder(new EmptyBorder(8, 14, 8, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean estaSeleccionado = Boolean.TRUE.equals(getClientProperty("seleccionado"));
            Color base = estaSeleccionado ? seleccionado : normal;

            if (getModel().isRollover() && !estaSeleccionado) {
                base = hover;
            }

            g2.setColor(new Color(0, 0, 0, estaSeleccionado ? 24 : 10));
            g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 6, 26, 26);

            GradientPaint fondo = new GradientPaint(
                    0, 0, vista.aclarar(base, 18),
                    0, getHeight(), base
            );

            g2.setPaint(fondo);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, 26, 26);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private class PanelRedondo extends JPanel {
        private final Color color;
        private final int arco;

        public PanelRedondo(Color color, int arco) {
            this.color = color;
            this.arco = arco;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arco, arco);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class GradientPanel extends JPanel {
        private final Color inicio;
        private final Color fin;

        public GradientPanel(Color inicio, Color fin) {
            this.inicio = inicio;
            this.fin = fin;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint fondo = new GradientPaint(0, 0, inicio, getWidth(), getHeight(), fin);
            g2.setPaint(fondo);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 38));
            for (int x = 18; x < getWidth(); x += 44) {
                g2.fillOval(x, 18, 7, 7);
                g2.fillOval(x + 20, 58, 4, 4);
            }

            g2.setColor(new Color(255, 255, 255, 28));
            g2.fillOval(getWidth() - 210, -80, 280, 280);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class PanelBanner extends JPanel {
        public PanelBanner() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(6, 9, w - 12, h - 12, 34, 34);

            GradientPaint fondo = new GradientPaint(
                    0, 0, new Color(255, 220, 238),
                    w, h, new Color(255, 247, 202)
            );

            g2.setPaint(fondo);
            g2.fillRoundRect(0, 0, w - 10, h - 14, 34, 34);

            g2.setColor(new Color(255, 255, 255, 115));
            g2.fillOval(w - 145, -40, 190, 190);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class PanelProducto extends JPanel {
        public PanelProducto() {
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(7, 10, w - 16, h - 18, 30, 30);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 4, w - 12, h - 18, 30, 30);

            g2.setColor(new Color(244, 222, 236));
            g2.drawRoundRect(0, 4, w - 13, h - 19, 30, 30);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}