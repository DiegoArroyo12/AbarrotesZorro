package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.EmpleadoRolEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRolRepository extends JpaRepository<EmpleadoRolEntity,Long> {
}
