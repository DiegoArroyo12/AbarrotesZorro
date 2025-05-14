package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.CajaEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CajaRepository extends JpaRepository<CajaEntity,Long> {

}
