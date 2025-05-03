package mx.unam.aragon.service.empleadorol;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.EmpleadoRolEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.EmpleadoRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoRolServiceImpl implements EmpleadoRolService {
    @Autowired
    EmpleadoRolRepository empleadoRolRepository;

    @Override
    @Transactional
    public EmpleadoRolEntity save(EmpleadoRolEntity empleado_rol) {
        return empleadoRolRepository.save(empleado_rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoRolEntity> findAll() {

        return empleadoRolRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        empleadoRolRepository.deleteById(id);
    }

    @Override
    public EmpleadoRolEntity findById(Long id) {
        Optional<EmpleadoRolEntity> op=empleadoRolRepository.findById(id);
        return op.orElse(null);
    }
}
