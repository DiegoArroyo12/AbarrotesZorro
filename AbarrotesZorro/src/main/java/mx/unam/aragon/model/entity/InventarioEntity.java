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
    @MapsId("id_almacen")
    @JoinColumn(name = "id_almacen")
    private AlmacenEntity almacen;

    @Column(name = "stock")
    private int stock;

}
