package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {
    @Query("SELECT i FROM InventarioEntity i WHERE i.almacen.id = :idAlmacen")
    List<InventarioEntity> findByAlmacen(Integer idAlmacen);
}
