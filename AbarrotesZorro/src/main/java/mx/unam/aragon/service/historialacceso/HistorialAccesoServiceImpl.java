package mx.unam.aragon.service.historialacceso;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.CajaEntity;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.HistorialAccesoEntity;
import mx.unam.aragon.repository.HistorialAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public void registrarAcceso(Integer idEmpleado, Integer idCaja, Integer idAlmacen) {
        HistorialAccesoEntity acceso = new HistorialAccesoEntity();
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setId(idEmpleado.longValue());
        acceso.setEmpleado(empleado);

        CajaEntity caja = new CajaEntity();
        caja.setId(idCaja.longValue());
        acceso.setCaja(caja);

        AlmacenEntity almacen = new AlmacenEntity();
        almacen.setId(idAlmacen.longValue());
        acceso.setAlmacen(almacen);

        acceso.setFecha_entrada(LocalDate.now());
        historialAccesoRepository.save(acceso);
    }
}
