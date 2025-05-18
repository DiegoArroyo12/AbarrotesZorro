package mx.unam.aragon.service.venta;

import mx.unam.aragon.model.entity.VentaEntity;

import java.util.List;

public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    VentaEntity findById(Long id);
    void deleteById(Long id);
}
