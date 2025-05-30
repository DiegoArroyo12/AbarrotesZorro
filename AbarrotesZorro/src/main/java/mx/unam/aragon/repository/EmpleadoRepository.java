package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity,Long> {
    Optional<EmpleadoEntity> findByUsuario(String usuario);

    List<EmpleadoEntity> findBySucursalIdAndActivoTrue(Integer idSucursal);

    boolean existsByUsuario(String usuario);
}
