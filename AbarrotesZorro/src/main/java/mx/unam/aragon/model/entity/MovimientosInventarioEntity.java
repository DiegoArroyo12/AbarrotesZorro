package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.unam.aragon.model.entity.enums.Tipo;

import java.time.LocalDate;

@Entity(name = "movimientos_inventarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimientosInventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private SucursalEntity almacen;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private Tipo tipo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "referencia")
    private String referencia;
}
