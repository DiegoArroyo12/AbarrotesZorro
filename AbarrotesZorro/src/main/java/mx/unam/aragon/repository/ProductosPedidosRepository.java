package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.ProductosPedidosEntity;
import mx.unam.aragon.model.entity.seriales.IdProductoSucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductosPedidosRepository extends JpaRepository<ProductosPedidosEntity, IdProductoSucursal> {
    List<ProductosPedidosEntity> findById_IdSucursal(Long idSucursal);
}
