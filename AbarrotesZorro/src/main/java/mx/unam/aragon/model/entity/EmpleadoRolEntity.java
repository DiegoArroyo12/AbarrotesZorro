package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "empleado_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpleadoRolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado_rol")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private RolEntity rol;
}
