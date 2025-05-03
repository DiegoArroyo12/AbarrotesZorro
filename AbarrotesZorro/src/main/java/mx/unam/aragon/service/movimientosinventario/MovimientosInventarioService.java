package mx.unam.aragon.service.movimientosinventario;

import mx.unam.aragon.model.entity.MovimientosInventarioEntity;

import java.util.List;

public interface MovimientosInventarioService {
    MovimientosInventarioEntity save(MovimientosInventarioEntity movimientos_inventario);
    List<MovimientosInventarioEntity> findAll();
    void deleteById(Long id);
    MovimientosInventarioEntity findById(Long id);
}
