package mx.unam.aragon.service.proveedor;

import mx.unam.aragon.model.entity.ProveedorEntity;

import java.util.List;

public interface ProveedorService {
    ProveedorEntity save(ProveedorEntity producto);
    List<ProveedorEntity> findAll();
    void deleteById(Long id);
    ProveedorEntity findById(Long id);
}
