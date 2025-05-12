package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlmacenRepository extends JpaRepository<AlmacenEntity,Long> {
    @Query("SELECT a.nombre FROM almacenes a WHERE a.id = :idAlmacen")
    String findNombreById(@Param("idAlmacen") Integer idAlmacen);
}
