package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @Column(name = "fecha_entrada")
    private LocalDate fecha_entrada;

    @Column(name = "fecha_salida")
    private LocalDate fecha_salida;
}
