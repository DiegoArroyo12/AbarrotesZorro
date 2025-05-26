package mx.unam.aragon.model.entity.seriales;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class IdProductoSucursal implements Serializable {

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "id_sucursal")
    private Long idSucursal;

    public IdProductoSucursal() {}

    public IdProductoSucursal(Long idProducto, Long idSucursal) {
        this.idProducto = idProducto;
        this.idSucursal = idSucursal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdProductoSucursal that = (IdProductoSucursal) o;
        return Objects.equals(idProducto, that.idProducto) && Objects.equals(idSucursal, that.idSucursal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idSucursal);
    }
}
