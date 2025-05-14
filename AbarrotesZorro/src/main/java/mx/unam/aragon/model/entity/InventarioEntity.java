package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import mx.unam.aragon.model.entity.seriales.IdInventario;

@Entity
@Table(name = "inventarios")
public class InventarioEntity {
    @EmbeddedId
    private IdInventario id;

    @ManyToOne
    @MapsId("id_producto")
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;

    @ManyToOne
    @MapsId("id_sucursal")
    @JoinColumn(name = "id_sucursal")
    private SucursalEntity sucursal;

    @Column(name = "stock")
    private int stock;

}
