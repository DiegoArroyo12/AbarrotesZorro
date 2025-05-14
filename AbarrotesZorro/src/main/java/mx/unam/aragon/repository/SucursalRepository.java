package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SucursalRepository extends JpaRepository<SucursalEntity,Long> {
    Optional<SucursalEntity> findById(Long id);
}
