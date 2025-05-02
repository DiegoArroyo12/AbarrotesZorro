package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}