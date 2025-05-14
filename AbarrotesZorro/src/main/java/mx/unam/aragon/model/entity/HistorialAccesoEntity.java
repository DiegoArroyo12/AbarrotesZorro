package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "historial_accesos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialAccesoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acceso")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;

    @ManyToOne
    @JoinColumn(name = "id_caja")
    private CajaEntity caja;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private AlmacenEntity almacen;

    @Column(name = "fecha_entrada")
    private LocalDateTime fechaEntrada;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;
}
