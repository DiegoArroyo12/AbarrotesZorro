package mx.unam.aragon.service.cliente;

import mx.unam.aragon.model.entity.ClienteEntity;
import mx.unam.aragon.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public ClienteEntity save(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public List<ClienteEntity> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public void deleteById(String correo) {
        clienteRepository.deleteById(correo);
    }

    @Override
    public ClienteEntity findByCorreo(String correo) { return clienteRepository.findById(correo).orElse(null); }

    @Override
    public ClienteEntity findByTelefono(String telefono) { return clienteRepository.findByTelefono(telefono).orElse(null); }
}
