package mx.unam.aragon.service.cliente;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.ClienteEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Override
    @Transactional
    public ClienteEntity save(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> findAll() {

        return clienteRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public ClienteEntity findById(Long id) {
        Optional<ClienteEntity> op=clienteRepository.findById(id);
        return op.orElse(null);
    }
}
