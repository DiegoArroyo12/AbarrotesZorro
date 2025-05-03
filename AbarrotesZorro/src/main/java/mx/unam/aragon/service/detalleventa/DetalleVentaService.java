package mx.unam.aragon.service.detalleventa;

import mx.unam.aragon.model.entity.DetalleVentaEntity;

import java.util.List;

public interface DetalleVentaService {
    DetalleVentaEntity save(DetalleVentaEntity detalle_venta);
    List<DetalleVentaEntity> findAll();
    void deleteById(Long id);
    DetalleVentaEntity findById(Long id);
}
