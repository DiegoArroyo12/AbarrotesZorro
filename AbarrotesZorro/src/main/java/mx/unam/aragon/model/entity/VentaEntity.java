package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "correo_cliente")
    private ClienteEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;

    @ManyToOne
    @JoinColumn(name = "id_caja")
    private CajaEntity caja;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fechaVenta;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVentaEntity> detalles;
}
