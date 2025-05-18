package mx.unam.aragon.service.venta;

import mx.unam.aragon.model.entity.VentaEntity;
import mx.unam.aragon.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    @Transactional
    public VentaEntity save(VentaEntity venta) {
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaEntity> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaEntity findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }
}
