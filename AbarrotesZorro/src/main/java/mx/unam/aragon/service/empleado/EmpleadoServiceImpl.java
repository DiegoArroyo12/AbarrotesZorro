package mx.unam.aragon.service.empleado;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {
    @Autowired
    EmpleadoRepository empleadoRepository;

    @Override
    @Transactional
    public EmpleadoEntity save(EmpleadoEntity empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoEntity> findAll() {

        return empleadoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        empleadoRepository.deleteById(id);
    }

    @Override
    public EmpleadoEntity findById(Long id) {
        Optional<EmpleadoEntity> op=empleadoRepository.findById(id);
        return op.orElse(null);
    }
}
