package mx.unam.aragon.service.historialacceso;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.HistorialAccesoEntity;

import java.util.List;

public interface HistorialAccesoService {
    HistorialAccesoEntity save(HistorialAccesoEntity historial_acceso);
    List<HistorialAccesoEntity> findAll();
    void deleteById(Long id);
    HistorialAccesoEntity findById(Long id);
}
