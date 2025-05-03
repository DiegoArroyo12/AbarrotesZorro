package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoEntity,Long> {
}
