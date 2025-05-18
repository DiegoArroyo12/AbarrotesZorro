package mx.unam.aragon.service.detalleventa;

import mx.unam.aragon.model.entity.DetalleVentaEntity;
import mx.unam.aragon.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    @Transactional
    public DetalleVentaEntity save(DetalleVentaEntity detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
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
    @Transactional(readOnly = true)
    public DetalleVentaEntity findById(Long id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }
}
