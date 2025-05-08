package mx.unam.aragon.service.cliente;

import mx.unam.aragon.model.entity.ClienteEntity;

import java.util.List;

public interface ClienteService {
    ClienteEntity save(ClienteEntity cliente);
    List<ClienteEntity> findAll();
    void deleteById(String correo);
    ClienteEntity findById(String correo);
}
