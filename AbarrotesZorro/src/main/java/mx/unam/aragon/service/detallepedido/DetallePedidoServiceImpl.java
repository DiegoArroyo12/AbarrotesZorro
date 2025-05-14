package mx.unam.aragon.service.detallepedido;

import mx.unam.aragon.model.entity.DetallePedidoEntity;
import mx.unam.aragon.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {
    @Autowired
    DetallePedidoRepository detallePedidoRepository;

    @Override
    @Transactional
    public DetallePedidoEntity save(DetallePedidoEntity detalle_pedido) {
        return detallePedidoRepository.save(detalle_pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findAll() {

        return detallePedidoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        detallePedidoRepository.deleteById(id);
    }

    @Override
    public DetallePedidoEntity findById(Long id) {
        Optional<DetallePedidoEntity> op=detallePedidoRepository.findById(id);
        return op.orElse(null);
    }
}
