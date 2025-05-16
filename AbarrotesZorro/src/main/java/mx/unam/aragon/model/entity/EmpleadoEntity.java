package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "empleados")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "password_hash")
    private String password_hash;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private SucursalEntity sucursal;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "empleado_roles",
            joinColumns = @JoinColumn(name = "id_empleado"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    @Builder.Default
    private Set<RolEntity> roles = new HashSet<>();
}