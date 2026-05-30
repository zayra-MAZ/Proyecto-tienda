package Tienda;

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class controlador {

    private final modelo modelo;
    private final vista vista;

    public controlador(modelo modelo, vista vista) {

        this.modelo = modelo;
        this.vista = vista;
        eventos();
        actualizarProductos();
    }

    private void eventos() {

        vista.getBuscador().getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                actualizarProductos();
            }

            public void removeUpdate(DocumentEvent e) {
                actualizarProductos();
            }

            public void changedUpdate(DocumentEvent e) {
                actualizarProductos();
            }
        });
        for (String nombreCategoria : vista.getBotonesDeCategorias().keySet()) {
            JButton boton = vista.getBotonesDeCategorias().get(nombreCategoria);
            boton.addActionListener(e -> {
                vista.seleccionaCategoria(nombreCategoria);
                actualizarProductos();

            });
        }
        vista.getCarritoDeCompras().addActionListener(e -> {
            vista.conmutarCarrito();

        });
    }

    private void actualizarProductos() {

        String categoria = vista.getCategoriasSelecc();
        String textoBusqueda = vista.getTextoBuscador();
        List<Producto> productosFiltrados =
                modelo.filtrarProductos(categoria, textoBusqueda);

        vista.mostrarproductos(productosFiltrados);
        configurarEventosDeLasTarjetas();
    }

    private void configurarEventosDeLasTarjetas() {

        List<JButton> botones =
                vista.getBotonesCarritoTarjetas();
        for (JButton botonCarrito : botones) {
            for (ActionListener al : botonCarrito.getActionListeners()) {
                botonCarrito.removeActionListener(al);
            }

            botonCarrito.addActionListener(e -> {
                Producto producto =
                        (Producto) botonCarrito.getClientProperty("productoAsociado");

                if (producto != null) {
                    vista.agregarProductoAlCarrito(producto);
                    modelo.agregarAlCarrito(producto);
                    vista.conmutarCarrito();
                }
            });
        }
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            modelo modelo = new modelo();
            vista vista =
                    new vista(modelo.obtenerCategorias());
            controlador controlador =
                    new controlador(modelo, vista);
            controlador.iniciar();
        });
    }
}