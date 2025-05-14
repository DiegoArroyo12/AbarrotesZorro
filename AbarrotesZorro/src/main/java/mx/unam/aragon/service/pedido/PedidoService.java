package mx.unam.aragon.service.pedido;

import mx.unam.aragon.model.entity.PedidoEntity;

import java.util.List;

public interface PedidoService {
    PedidoEntity save(PedidoEntity pedido);
    List<PedidoEntity> findAll();
    void deleteById(Long id);
    PedidoEntity findById(Long id);
}
