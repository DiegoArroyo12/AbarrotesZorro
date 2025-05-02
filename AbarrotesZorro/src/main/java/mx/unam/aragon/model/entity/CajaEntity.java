package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cajas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CajaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caja")
    private Long id;

    @Column(name = "nombre")
    private String nombre;
}
