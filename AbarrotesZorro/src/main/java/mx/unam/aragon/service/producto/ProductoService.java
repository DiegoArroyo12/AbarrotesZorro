package mx.unam.aragon.service.producto;

import mx.unam.aragon.model.entity.ProductoEntity;

import java.util.List;

public interface ProductoService {
    ProductoEntity save(ProductoEntity producto);
    List<ProductoEntity> findAll();
    void deleteById(Long id);
    ProductoEntity findById(Long id);
}
