package mx.unam.aragon.service.historialacceso;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.HistorialAccesoEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.HistorialAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialAccesoServiceImpl implements HistorialAccesoService {
    @Autowired
    HistorialAccesoRepository historialAccesoRepository;

    @Override
    @Transactional
    public HistorialAccesoEntity save(HistorialAccesoEntity historial_acceso) {
        return historialAccesoRepository.save(historial_acceso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialAccesoEntity> findAll() {

        return historialAccesoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        historialAccesoRepository.deleteById(id);
    }

    @Override
    public HistorialAccesoEntity findById(Long id) {
        Optional<HistorialAccesoEntity> op=historialAccesoRepository.findById(id);
        return op.orElse(null);
    }
}
