package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Double precioUnitario;
}
