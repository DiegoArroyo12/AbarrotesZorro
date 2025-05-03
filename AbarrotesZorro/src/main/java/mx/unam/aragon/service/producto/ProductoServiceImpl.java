package mx.unam.aragon.service.producto;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.model.entity.ProductoEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    ProductoRepository productoRepository;

    @Override
    @Transactional
    public ProductoEntity save(ProductoEntity producto) {

        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findAll() {

        return productoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoEntity findById(Long id) {
        Optional<ProductoEntity> op=productoRepository.findById(id);
        return op.orElse(null);
    }
}
