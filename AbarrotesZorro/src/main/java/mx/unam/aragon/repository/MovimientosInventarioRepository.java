package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.MovimientosInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientosInventarioRepository extends JpaRepository<MovimientosInventarioEntity,Long> {
}
