package mx.unam.aragon.service.sucursal;

import mx.unam.aragon.model.entity.SucursalEntity;

import java.util.List;

public interface SucursalService {
    SucursalEntity save(SucursalEntity sucursal);
    List<SucursalEntity> findAll();
    void deleteById(Long id);
    SucursalEntity findById(Long id);
}
