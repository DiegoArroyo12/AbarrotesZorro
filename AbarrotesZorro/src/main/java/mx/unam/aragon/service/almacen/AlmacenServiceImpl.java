package mx.unam.aragon.service.almacen;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenServiceImpl implements AlmacenService {
    @Autowired
    AlmacenRepository almacenRepository;

    @Override
    @Transactional
    public AlmacenEntity save(AlmacenEntity almacen) {
        return almacenRepository.save(almacen);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenEntity> findAll() {

        return almacenRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        almacenRepository.deleteById(id);
    }

    @Override
    public AlmacenEntity findById(Long id) {
        Optional<AlmacenEntity> op=almacenRepository.findById(id);
        return op.orElse(null);
    }
}
