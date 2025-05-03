package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmacenRepository extends JpaRepository<AlmacenEntity,Long> {
}
