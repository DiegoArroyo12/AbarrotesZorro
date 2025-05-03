package mx.unam.aragon.service.almacen;

import mx.unam.aragon.model.entity.AlmacenEntity;

import java.util.List;

public interface AlmacenService {
    AlmacenEntity save(AlmacenEntity actor);
    List<AlmacenEntity> findAll();
    void deleteById(Long id);
    AlmacenEntity findById(Long id);
}
