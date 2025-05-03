package mx.unam.aragon.service.pedido;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.PedidoEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;

    @Override
    @Transactional
    public PedidoEntity save(PedidoEntity pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findAll() {

        return pedidoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public PedidoEntity findById(Long id) {
        Optional<PedidoEntity> op=pedidoRepository.findById(id);
        return op.orElse(null);
    }
}
