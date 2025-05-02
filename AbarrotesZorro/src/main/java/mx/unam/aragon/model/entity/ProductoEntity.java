package mx.unam.aragon.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Entity(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "imagen")
    @Lob
    private Blob imagen;

    @Column(name ="precio")
    private Double precio;

}