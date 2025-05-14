package mx.unam.aragon.service.detalleventa;

import mx.unam.aragon.model.entity.DetalleVentaEntity;
import mx.unam.aragon.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {
    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    @Override
    @Transactional
    public DetalleVentaEntity save(DetalleVentaEntity detalle_venta) {
        return detalleVentaRepository.save(detalle_venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaEntity> findAll() {

        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    public DetalleVentaEntity findById(Long id) {
        Optional<DetalleVentaEntity> op=detalleVentaRepository.findById(id);
        return op.orElse(null);
    }
}
