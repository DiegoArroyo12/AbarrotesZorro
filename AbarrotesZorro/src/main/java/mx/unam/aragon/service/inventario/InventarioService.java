package mx.unam.aragon.service.inventario;

import mx.unam.aragon.model.entity.InventarioEntity;

import java.util.List;

public interface InventarioService {
    InventarioEntity save(InventarioEntity inventario);
    List<InventarioEntity> findAll();
    void deleteById(Long idProducto, Long idSucursal);

    InventarioEntity findById(Long idProducto, Long idSucursal);
}
