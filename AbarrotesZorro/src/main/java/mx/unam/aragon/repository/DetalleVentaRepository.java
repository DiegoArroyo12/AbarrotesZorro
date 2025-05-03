package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.DetalleVentaEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity,Long> {
}
