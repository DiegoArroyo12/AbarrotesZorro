package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoEntity,Long> {
}
