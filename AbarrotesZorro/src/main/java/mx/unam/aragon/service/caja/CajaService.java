package mx.unam.aragon.service.caja;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.CajaEntity;

import java.util.List;

public interface CajaService {
    CajaEntity save(CajaEntity caja);
    List<CajaEntity> findAll();
    void deleteById(Long id);
    CajaEntity findById(Long id);
}
