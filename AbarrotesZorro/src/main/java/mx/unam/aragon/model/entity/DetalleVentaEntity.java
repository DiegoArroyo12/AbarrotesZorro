package mx.unam.aragon.model.entity;
import mx.unam.aragon.model.entity.VentaEntity;
import mx.unam.aragon.model.entity.ProductoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "detalle_ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private VentaEntity venta;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Integer precio_unitario;
}