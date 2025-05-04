package mx.unam.aragon.service.empleado;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.EmpleadoRolEntity;

import java.util.List;

public interface EmpleadoService {
    EmpleadoEntity save(EmpleadoEntity empleado_rol);
    List<EmpleadoEntity> findAll();
    void deleteById(Long id);
    EmpleadoEntity findById(Long id);
}
