package mx.unam.aragon.repository;

import mx.unam.aragon.model.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, String> {
    Optional<ClienteEntity> findByTelefono(String telefono);

    Optional<ClienteEntity> findByCorreo(String correo);
}
