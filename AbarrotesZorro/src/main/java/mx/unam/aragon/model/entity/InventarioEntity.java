package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.unam.aragon.model.entity.seriales.IdInventario;

@Entity(name = "inventarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventarioEntity {
    @EmbeddedId
    private IdInventario id;

    @ManyToOne
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;

    @ManyToOne
    @MapsId("idSucursal")
    @JoinColumn(name = "id_sucursal")
    private SucursalEntity sucursal;

    @Column(name = "stock")
    private int stock;
}
