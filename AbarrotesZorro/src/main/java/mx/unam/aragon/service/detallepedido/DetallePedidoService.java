package mx.unam.aragon.service.detallepedido;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.DetallePedidoEntity;

import java.util.List;

public interface DetallePedidoService {
    DetallePedidoEntity save(DetallePedidoEntity detalle_pedido);
    List<DetallePedidoEntity> findAll();
    void deleteById(Long id);
    DetallePedidoEntity findById(Long id);
}
