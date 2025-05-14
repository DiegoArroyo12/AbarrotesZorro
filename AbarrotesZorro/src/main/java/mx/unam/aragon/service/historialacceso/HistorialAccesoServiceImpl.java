package mx.unam.aragon.service.historialacceso;

import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.model.entity.CajaEntity;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.HistorialAccesoEntity;
import mx.unam.aragon.repository.EmpleadoRepository;
import mx.unam.aragon.repository.HistorialAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistorialAccesoServiceImpl implements HistorialAccesoService {
    @Autowired
    HistorialAccesoRepository historialAccesoRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;

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
    public void registrarEntradaInicial(String username) {
        Optional<EmpleadoEntity> empleadoOpt = empleadoRepository.findByUsuario(username);
        if (empleadoOpt.isPresent()) {
            EmpleadoEntity empleado = empleadoOpt.get();

            HistorialAccesoEntity acceso = new HistorialAccesoEntity();
            acceso.setEmpleado(empleado);
            acceso.setFechaEntrada(LocalDateTime.now());

            historialAccesoRepository.save(acceso);
        }
    }

    @Override
    @Transactional
    public void registrarAcceso(Integer idEmpleado, Integer idCaja, Integer idSucursal) {
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setId(idEmpleado.longValue());

        // Buscar el último registro de acceso sin almacén ni caja
        HistorialAccesoEntity ultimoAcceso = historialAccesoRepository.findTopByEmpleadoOrderByFechaEntradaDesc(empleado);

        if (ultimoAcceso != null && ultimoAcceso.getSucursal() == null && ultimoAcceso.getCaja() == null) {
            CajaEntity caja = new CajaEntity();
            caja.setId(idCaja.longValue());
            ultimoAcceso.setCaja(caja);

            SucursalEntity sucursal = new SucursalEntity();
            sucursal.setId(idSucursal.longValue());
            ultimoAcceso.setSucursal(sucursal);

            historialAccesoRepository.save(ultimoAcceso);
        }
    }

    @Override
    @Transactional
    public void registrarSalida(String username) {
        Optional<EmpleadoEntity> empleadoOpt = empleadoRepository.findByUsuario(username);
        if (empleadoOpt.isPresent()) {
            EmpleadoEntity empleado = empleadoOpt.get();

            HistorialAccesoEntity ultimoAcceso = historialAccesoRepository.findTopByEmpleadoOrderByFechaEntradaDesc(empleado);

            if (ultimoAcceso != null) {
                ultimoAcceso.setFechaSalida(LocalDateTime.now());
                historialAccesoRepository.save(ultimoAcceso);
            }
        }
    }
}
