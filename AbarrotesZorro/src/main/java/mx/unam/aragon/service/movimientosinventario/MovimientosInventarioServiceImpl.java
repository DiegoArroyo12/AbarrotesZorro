package mx.unam.aragon.service.movimientosinventario;

import mx.unam.aragon.model.entity.MovimientosInventarioEntity;
import mx.unam.aragon.repository.MovimientosInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientosInventarioServiceImpl implements MovimientosInventarioService {
    @Autowired
    MovimientosInventarioRepository movimientosInventarioRepository;

    @Override
    @Transactional
    public MovimientosInventarioEntity save(MovimientosInventarioEntity movimientos_inventario) {
        return movimientosInventarioRepository.save(movimientos_inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosInventarioEntity> findAll() {

        return movimientosInventarioRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        movimientosInventarioRepository.deleteById(id);
    }

    @Override
    public MovimientosInventarioEntity findById(Long id) {
        Optional<MovimientosInventarioEntity> op=movimientosInventarioRepository.findById(id);
        return op.orElse(null);
    }
}
