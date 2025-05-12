package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.InventarioEntity;
import mx.unam.aragon.model.entity.view.ProductoInventarioView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {
    @Query("SELECT i FROM InventarioEntity i WHERE i.almacen.id = :idAlmacen")
    List<InventarioEntity> findByAlmacen(Integer idAlmacen);

    @Query("SELECT a.nombre FROM InventarioEntity i JOIN i.almacen a WHERE a.id = :idAlmacen")
    String findNombreById(Integer idAlmacen);

    @Query("""
        SELECT p.nombre AS nombre, p.imagen AS imagen, i.stock AS stock, p.precio AS precio
        FROM InventarioEntity i
        JOIN i.producto p
        WHERE i.almacen.id = :idAlmacen
    """)
    List<ProductoInventarioView> findProductosPorAlmacen(@Param("idAlmacen") Integer idAlmacen);
}
