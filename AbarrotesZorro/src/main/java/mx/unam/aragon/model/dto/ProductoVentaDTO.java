package mx.unam.aragon.model.dto;

public class ProductoVentaDTO {
    private String nombre;
    private double precio;
    private int cantidad;
    private String imagen;
    private Long idDetalle;

    public ProductoVentaDTO() {

    }

    public ProductoVentaDTO(String nombre, double precio, int cantidad, String imagen, Long idDetalle) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagen = imagen;
        this.idDetalle = idDetalle;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
}
