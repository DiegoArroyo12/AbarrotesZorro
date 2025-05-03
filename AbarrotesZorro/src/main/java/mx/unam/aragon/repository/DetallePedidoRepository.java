package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.DetallePedidoEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity,Long> {
}
