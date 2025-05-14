package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.HistorialAccesoEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialAccesoRepository extends JpaRepository<HistorialAccesoEntity,Long> {
    HistorialAccesoEntity findTopByEmpleadoOrderByFechaEntradaDesc(EmpleadoEntity empleado);
}
