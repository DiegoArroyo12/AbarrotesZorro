package mx.unam.aragon.service.venta;

import mx.unam.aragon.model.entity.VentaEntity;

import java.util.List;

public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    void deleteById(Long id);
    VentaEntity findById(Long id);
}
