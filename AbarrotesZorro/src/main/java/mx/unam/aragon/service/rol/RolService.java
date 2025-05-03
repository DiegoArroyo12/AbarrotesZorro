package mx.unam.aragon.service.rol;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.RolEntity;

import java.util.List;

public interface RolService {
    RolEntity save(RolEntity rol);
    List<RolEntity> findAll();
    void deleteById(Long id);
    RolEntity findById(Long id);
}
