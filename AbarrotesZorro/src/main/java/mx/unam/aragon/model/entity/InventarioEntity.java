package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "inventarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private AlmacenEntity almacen;

}
