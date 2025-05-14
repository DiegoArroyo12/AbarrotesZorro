package mx.unam.aragon.service.sucursal;

import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalServiceImpl implements SucursalService {
    @Autowired
    SucursalRepository sucursalRepository;

    @Override
    @Transactional
    public SucursalEntity save(SucursalEntity sucursal) {
        return sucursalRepository.save(sucursal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SucursalEntity> findAll() {

        return sucursalRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        sucursalRepository.deleteById(id);
    }

    @Override
    public SucursalEntity findById(Long id) {
        Optional<SucursalEntity> op= sucursalRepository.findById(id);
        return op.orElse(null);
    }
}
