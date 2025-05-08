package mx.unam.aragon.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name= "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteEntity {

    @Id
    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "telefono")
    private String telefono;


}