package mx.unam.aragon.service.venta;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.VentaEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    VentaRepository ventaRepository;

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
    @Transactional
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public VentaEntity findById(Long id) {
        Optional<VentaEntity> op=ventaRepository.findById(id);
        return op.orElse(null);
    }
}
