package mx.unam.aragon.controller.producto;

import mx.unam.aragon.model.entity.ProductoEntity;
import mx.unam.aragon.model.entity.view.ProductoInventarioView;
import mx.unam.aragon.repository.InventarioRepository;
import mx.unam.aragon.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/abarrotes/productos")
public class ProductoRestController {

    @Autowired
    private InventarioRepository inventarioRepository;

    @GetMapping
    public List<ProductoInventarioView> obtenerProductos(@RequestParam("idSucursal") Integer idSucursal) {
        return inventarioRepository.findProductosPorSucursal(idSucursal);
    }
}
