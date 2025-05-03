package mx.unam.aragon.service.inventario;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements InventarioService {
    @Autowired
    InventarioRepository inventarioRepository;

    @Override
    @Transactional
    public InventarioEntity save(InventarioEntity inventario) {

        return inventarioRepository.save(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioEntity> findAll() {

        return inventarioRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        inventarioRepository.deleteById(id);
    }

    @Override
    public InventarioEntity findById(Long id) {
        Optional<InventarioEntity> op=inventarioRepository.findById(id);
        return op.orElse(null);
    }
}
