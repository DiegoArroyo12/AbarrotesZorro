package mx.unam.aragon.service.cliente;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.ClienteEntity;

import java.util.List;

public interface ClienteService {
    ClienteEntity save(ClienteEntity cliente);
    List<ClienteEntity> findAll();
    void deleteById(Long id);
    ClienteEntity findById(Long id);
}
