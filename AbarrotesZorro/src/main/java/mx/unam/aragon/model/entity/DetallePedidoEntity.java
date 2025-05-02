package mx.unam.aragon.model.entity;
import mx.unam.aragon.model.entity.PedidoEntity;
import mx.unam.aragon.model.entity.ProductoEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "detalle_pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoEntity pedido;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precio_unitario;
}