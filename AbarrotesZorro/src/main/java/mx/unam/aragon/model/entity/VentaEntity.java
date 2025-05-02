package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @Column(name = "correo_cliente")
    private String correo_cliente;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoEntity empleado;

    @ManyToOne
    @JoinColumn(name = "id_caja")
    private CajaEntity caja;

    @Column(name = "total")
    private Double total;

    @Column(name = "fecha")
    private LocalDate fecha;
}
