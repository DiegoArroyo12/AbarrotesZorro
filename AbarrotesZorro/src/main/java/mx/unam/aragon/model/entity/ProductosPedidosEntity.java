package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.unam.aragon.model.entity.seriales.IdProductoSucursal;

@Entity(name = "productos_pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductosPedidosEntity {

    @EmbeddedId
    private IdProductoSucursal id;

    @ManyToOne
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private ProductoEntity producto;

    @ManyToOne
    @JoinColumn(name = "id_sucursal", insertable = false, updatable = false)
    private SucursalEntity sucursal;

    @Column(name = "cantidad")
    private int cantidad;
}