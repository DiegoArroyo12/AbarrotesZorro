package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<ProductoEntity,Long> {
    Optional<ProductoEntity> findByNombre(String nombre); // ← este es el método que te falta

}
