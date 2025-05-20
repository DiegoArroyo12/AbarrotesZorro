package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.InventarioEntity;
import mx.unam.aragon.model.entity.view.ProductoInventarioView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {
    @Query("SELECT i FROM inventarios i WHERE i.sucursal.id = :idSucursal")
    List<InventarioEntity> findBySucursal(@Param("idSucursal") Integer idSucursal);

    @Query("SELECT a.nombre FROM inventarios i JOIN i.sucursal a WHERE a.id = :idSucursal")
    String findNombreById(Integer idSucursal);

    @Query("""
        SELECT p.id AS id, p.nombre AS nombre, p.imagen AS imagen, i.stock AS stock, p.precio AS precio
        FROM inventarios i
        JOIN i.producto p
        WHERE i.sucursal.id = :idSucursal
    """)
    List<ProductoInventarioView> findProductosPorSucursal(@Param("idSucursal") Integer idSucursal);

    InventarioEntity findByProductoIdAndSucursalId(Integer idProducto, Integer idSucursal);
}
