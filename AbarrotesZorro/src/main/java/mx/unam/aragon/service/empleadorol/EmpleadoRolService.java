package mx.unam.aragon.service.empleadorol;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.EmpleadoRolEntity;

import java.util.List;

public interface EmpleadoRolService {
    EmpleadoRolEntity save(EmpleadoRolEntity empleado_rol);
    List<EmpleadoRolEntity> findAll();
    void deleteById(Long id);
    EmpleadoRolEntity findById(Long id);
}
