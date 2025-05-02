package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import mx.unam.aragon.model.entity.seriales.IdEmpleadoRol;

@Entity
@Table(name = "empleado_roles")
public class EmpleadoRolEntity {
    @EmbeddedId
    private IdEmpleadoRol id;

    @ManyToOne
    @MapsId("id_empleado")
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;

    @ManyToOne
    @MapsId("id_rol")
    @JoinColumn(name = "id_rol")
    private RolEntity rol;
}

