package mx.unam.aragon.model.entity.seriales;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class IdInventario implements Serializable {
    private Integer id_producto;
    private Integer id_almacen;
}
