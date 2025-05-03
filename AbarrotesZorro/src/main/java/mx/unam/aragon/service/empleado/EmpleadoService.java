package mx.unam.aragon.service.empleado;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.EmpleadoRolEntity;

import java.util.List;

public interface EmpleadoService {
    EmpleadoRolEntity save(EmpleadoRolEntity empleado_rol);
    List<EmpleadoRolEntity> findAll();
    void deleteById(Long id);
    EmpleadoRolEntity findById(Long id);
}
