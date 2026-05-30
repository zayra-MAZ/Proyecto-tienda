package Tienda;

public class Producto {

    private String nombre;
    private String categoria;
    private double precio;
    private String rutaImagen;

    public Producto(String nombre, String categoria, double precio, String rutaImagen) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}