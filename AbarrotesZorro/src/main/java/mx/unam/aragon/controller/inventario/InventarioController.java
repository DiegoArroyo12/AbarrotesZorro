package mx.unam.aragon.controller.inventario;

import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.model.entity.view.ProductoInventarioView;
import mx.unam.aragon.repository.SucursalRepository;
import mx.unam.aragon.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InventarioController {

    @Autowired
    InventarioRepository inventarioRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @GetMapping("/inventario")
    public String mostrarInventario(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        model.addAttribute("inventario", inventarioRepository.findByAlmacen(idSucursal));
        model.addAttribute("idSucursal", idSucursal);

        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal)).map(SucursalEntity::getNombre).orElse("Nombre no encontrado");
        List<ProductoInventarioView> productos = inventarioRepository.findProductosPorAlmacen(idSucursal);

        model.addAttribute("nombreSucursal", nombreSucursal);
        model.addAttribute("productos", productos);
        model.addAttribute("nombreSucursal", nombreSucursal);
        return "inventario";
    }
}
