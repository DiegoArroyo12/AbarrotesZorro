package mx.unam.aragon.controller.producto;

import mx.unam.aragon.model.entity.ProductoEntity;
import mx.unam.aragon.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/abarrotes/productos")
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<ProductoEntity> obtenerProductos() {
        return productoRepository.findAll();
    }
}
