package mx.unam.aragon.service.caja;

import mx.unam.aragon.model.entity.CajaEntity;
import mx.unam.aragon.repository.CajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CajaServiceImpl implements CajaService {
    @Autowired
    CajaRepository cajaRepository;

    @Override
    @Transactional
    public CajaEntity save(CajaEntity caja) {
        return cajaRepository.save(caja);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaEntity> findAll() {

        return cajaRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cajaRepository.deleteById(id);
    }

    @Override
    public CajaEntity findById(Long id) {
        Optional<CajaEntity> op=cajaRepository.findById(id);
        return op.orElse(null);
    }
}
