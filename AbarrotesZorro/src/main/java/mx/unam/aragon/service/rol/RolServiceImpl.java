package mx.unam.aragon.service.rol;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.RolEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {
    @Autowired
    RolRepository rolRepository;

    @Override
    @Transactional
    public RolEntity save(RolEntity rol) {
        return rolRepository.save(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolEntity> findAll() {

        return rolRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public RolEntity findById(Long id) {
        Optional<RolEntity> op=rolRepository.findById(id);
        return op.orElse(null);
    }
}
