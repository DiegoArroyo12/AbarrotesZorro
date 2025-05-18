package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.HistorialAccesoEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialAccesoRepository extends JpaRepository<HistorialAccesoEntity,Long> {
    HistorialAccesoEntity findTopByEmpleadoOrderByFechaEntradaDesc(EmpleadoEntity empleado);

    @Query("SELECT h FROM historial_accesos h " +
            "JOIN FETCH h.empleado e " +
            "LEFT JOIN FETCH h.caja c " +
            "WHERE e.sucursal.id = :idSucursal " +
            "ORDER BY h.fechaEntrada DESC")
    List<HistorialAccesoEntity> findBySucursalWithDetails(@Param("idSucursal") Integer idSucursal);
}
