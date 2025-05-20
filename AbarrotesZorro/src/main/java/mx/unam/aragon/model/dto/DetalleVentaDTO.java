package mx.unam.aragon.model.dto;

public class DetalleVentaDTO {

    private String nombre;
    private int cantidad;
    private double precio;
    private String imagen;

    //constructor
    public DetalleVentaDTO(String nombre, int cantidad, double precio, String imagen) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public double getSubtotal() {
        return precio * cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen() { return imagen;}
    public void setImagen(String imagen) { this.imagen = imagen; }
}
