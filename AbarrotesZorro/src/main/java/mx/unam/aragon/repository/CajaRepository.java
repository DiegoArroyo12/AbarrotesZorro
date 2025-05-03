package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.CajaEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CajaRepository extends JpaRepository<CajaEntity,Long> {
}
