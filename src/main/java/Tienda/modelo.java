package Tienda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class modelo {

    private List<Producto> carrito = new ArrayList<>();
    private final List<Producto> productos;
    private final List<String> categorias;

    public modelo() {
        productos = new ArrayList<>();
        categorias = new ArrayList<>();

        cargarCategoriasDesdeDB();
        cargarProductosDesdeDB();
    }

    public modelo(boolean esModoPrueba) {
        this.productos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        
        categorias.add("Todos");
        categorias.add("Limpieza");
        categorias.add("Alimentos");
        categorias.add("Golosinas");
        categorias.add("Bebidas");
        
        inyectarProductosSimulados();
    }
    private void cargarCategoriasDesdeDB() {
        String sql = "SELECT DISTINCT categoria FROM productos ORDER BY categoria";
        try (
            Connection con = Conexion.conectar();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            if (con == null) throw new SQLException("No se pudo establecer conexión.");
            
            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }
        } catch (SQLException ex) {
            System.out.println("Error cargando categorías de la BD: " + ex.getMessage());
            if (categorias.isEmpty()) {
                categorias.add("Limpieza");
                categorias.add("Alimentos");
                categorias.add("Golosinas");
                categorias.add("Bebidas");
            }
        }
    }

    private void cargarProductosDesdeDB() {
        productos.clear();
        String sql = "SELECT nombre, categoria, precio, ruta_imagen FROM productos ORDER BY categoria, nombre";
        try (
            Connection con = Conexion.conectar();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            if (con == null) throw new SQLException("Base de datos desconectada.");

            while (rs.next()) {
                productos.add(new Producto(
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getString("ruta_imagen")
                ));
            }
            System.out.println("¡Éxito! Se cargaron " + productos.size() + " productos desde MySQL.");
            
        } catch (SQLException ex) {
            System.out.println("Aviso: No se pudo leer MySQL (" + ex.getMessage() + "). Cargando respaldo local.");

            inyectarProductosSimulados();
        }
    }
    private void inyectarProductosSimulados() {
        this.productos.add(new Producto("Jabón Zote", "Limpieza", 18.50, "/imagenes/Zote.png"));
        this.productos.add(new Producto("Detergente Liquid", "Limpieza", 45.00, "/imagenes/detergente.png"));
        this.productos.add(new Producto("Cloro", "Limpieza", 22.50, "/imagenes/cloro.png"));
        this.productos.add(new Producto("Fabuloso", "Limpieza", 33.70, "/imagenes/f.png"));
        this.productos.add(new Producto("Pinol", "Limpieza", 38.50, "/imagenes/p.png"));
        this.productos.add(new Producto("Limpiador para Vidrios", "Limpieza", 42.00, "/imagenes/v.png"));
        this.productos.add(new Producto("Ariel", "Limpieza", 47.90, "/imagenes/A.png"));
        this.productos.add(new Producto("Cuidado Superior Flor de Primavera", "Limpieza", 74.50, "/imagenes/S.png"));
        this.productos.add(new Producto("Limpia tu Inodoro con Harpic", "Limpieza", 49.00, "/imagenes/H.png"));
        this.productos.add(new Producto("Pato Pastilla Desinfectante", "Limpieza", 28.00, "/imagenes/pp.png"));
        this.productos.add(new Producto("Roma Detergente", "Limpieza", 33.70, "/imagenes/R.png"));
        this.productos.add(new Producto("Desinfectante en Aerosol", "Limpieza", 85.00, "/imagenes/D.png"));

        this.productos.add(new Producto("Verde Valle Arroz Súper Extra", "Alimentos", 28.00, "/imagenes/Arroz.png"));
        this.productos.add(new Producto("Nescafe Café Soluble ", "Alimentos", 175.00, "/imagenes/nes.png"));
        this.productos.add(new Producto("Cereal Integral Nestlé ", "Alimentos", 64.40, "/imagenes/cer.png"));
        this.productos.add(new Producto("La Lechera Nestlé ", "Alimentos", 24.60, "/imagenes/lec.png"));
        this.productos.add(new Producto("Verde Valle Queretaro Frijol Negro", "Alimentos", 42.90, "/imagenes/vaññ.png"));
        this.productos.add(new Producto("Caldo de Pollo Knorr", "Alimentos", 20.35, "/imagenes/cual.png"));
        this.productos.add(new Producto("Samyang Buldak Ramen Picante ", "Alimentos", 58.00, "/imagenes/ram.png"));
        this.productos.add(new Producto(" Aceite Comestible Vegetal ", "Alimentos", 33.60, "/imagenes/ace.png"));
        this.productos.add(new Producto("Yoplait Yogurth Batido Sabor Fresa", "Alimentos", 61.35, "/imagenes/yo.png"));
        this.productos.add(new Producto("Leche Lala Entera ", "Alimentos", 23.00, "/imagenes/leee.png"));
        this.productos.add(new Producto(" Tomates Molidos ", "Alimentos", 8.00, "/imagenes/tom.png"));
        this.productos.add(new Producto("Yemina Pasta Spaghetti ", "Alimentos", 8.00, "/imagenes/sp.png"));

        this.productos.add(new Producto("Agua 1L", "Bebidas", 15.00, "/imagenes/c.png"));
        this.productos.add(new Producto("Powerade Ion4", "Bebidas", 20.00, "/imagenes/pow.png"));
        this.productos.add(new Producto("Fuze Tea", "Bebidas", 20.90, "/imagenes/te.png"));
        this.productos.add(new Producto("Jumex Prisma Mango", "Bebidas", 25.90, "/imagenes/ma.png"));
        this.productos.add(new Producto("Flashlyte Suero", "Bebidas", 20.00, "/imagenes/fl.png"));
        this.productos.add(new Producto("Energética Monster Energy", "Bebidas", 41.50, "/imagenes/mos.png"));
        this.productos.add(new Producto("Refresco Coca-Cola", "Bebidas", 20.00, "/imagenes/co.png"));
        this.productos.add(new Producto("Pepsi Regular", "Bebidas", 18.90, "/imagenes/pes.png"));
        this.productos.add(new Producto("Rehidratante Electrolit Uva", "Bebidas", 33.00, "/imagenes/ele.png"));
        this.productos.add(new Producto("Jugo Del Valle", "Bebidas", 26.00, "/imagenes/ju.png"));
        this.productos.add(new Producto("Lotte Soju Chum Churum", "Bebidas", 77.90, "/imagenes/k.png"));
        this.productos.add(new Producto("Refresco Sabor Lima-Limón", "Bebidas", 26.00, "/imagenes/lll.png"));

        this.productos.add(new Producto("Takis Fuego Botana", "Golosinas", 20.00, "/imagenes/tak.png"));
        this.productos.add(new Producto("Panditas, Gomitas de sabores frutales", "Golosinas", 19.00, "/imagenes/go.png"));
        this.productos.add(new Producto("Kinder Delice", "Golosinas", 14.00, "/imagenes/ch.png"));
        this.productos.add(new Producto("Donas de Azúcar Bimbo ", "Golosinas", 25.90, "/imagenes/bon.png"));
        this.productos.add(new Producto("Picafresa gigante, Gomita", "Golosinas", 110.70, "/imagenes/gi.png"));
        this.productos.add(new Producto("Arcoiris Sabor a Fresa", "Golosinas", 42.00, "/imagenes/arco.png"));
        this.productos.add(new Producto("Chips Ahoy!", "Golosinas", 30.50, "/imagenes/gomi.png"));
        this.productos.add(new Producto("M&Ms Chocolate Chocolate", "Golosinas", 20.00, "/imagenes/mg.png"));
        this.productos.add(new Producto("Skittles Confites ", "Golosinas", 25.10, "/imagenes/sk.png"));
        this.productos.add(new Producto("Sonric's Rockaleta Bola", "Golosinas", 82.00, "/imagenes/rok.png"));
        this.productos.add(new Producto("Pastillas refrescantes Tic Tac Menta", "Golosinas", 13.80, "/imagenes/tic.png"));
        this.productos.add(new Producto("Betalina", "Golosinas", 25.00, "/imagenes/betalina.png"));
    }

    public void recargarProductos() { cargarProductosDesdeDB(); }
    public void agregarAlCarrito(Producto p) { carrito.add(p); }
    public List<Producto> obtenerCarrito() { return new ArrayList<>(carrito); }
    public List<String> obtenerCategorias() { return new ArrayList<>(categorias); }
    public List<Producto> obtenerProductos() { return new ArrayList<>(productos); }

    public void agregarProducto(String nombre, String categoria, double precio, String rutaImagen) {
        productos.add(new Producto(nombre, categoria, precio, rutaImagen));
    }

    public List<Producto> filtrarProductos(String categoria, String textoBusqueda) {
        List<Producto> resultado = new ArrayList<>();
        String categoriaFiltro = (categoria == null) ? "Todos" : categoria.trim();
        String texto = (textoBusqueda == null) ? "" : textoBusqueda.trim().toLowerCase();

        for (Producto producto : productos) {
            boolean coincideCategoria = categoriaFiltro.equalsIgnoreCase("Todos")
                    || producto.getCategoria().equalsIgnoreCase(categoriaFiltro);

            boolean coincideBusqueda = texto.isEmpty()
                    || producto.getNombre().toLowerCase().contains(texto)
                    || producto.getCategoria().toLowerCase().contains(texto);

            if (coincideCategoria && coincideBusqueda) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        if (categoria == null || categoria.equalsIgnoreCase("Todos")) {
            return obtenerProductos();
        }
        for (Producto producto : productos) {
            if (producto.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    public void eliminarProducto(String nombre) {
        productos.removeIf(producto -> producto.getNombre().equalsIgnoreCase(nombre));
    }

    public void limpiarProductos() { productos.clear(); }
}