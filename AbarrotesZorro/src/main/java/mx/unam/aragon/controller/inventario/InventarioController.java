package mx.unam.aragon.controller.inventario;

import mx.unam.aragon.model.entity.view.ProductoInventarioView;
import mx.unam.aragon.repository.AlmacenRepository;
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
    AlmacenRepository almacenRepository;

    @GetMapping("/inventario")
    public String mostrarInventario(@RequestParam("idAlmacen") Integer idAlmacen, Model model) {
        model.addAttribute("inventario", inventarioRepository.findByAlmacen(idAlmacen));
        model.addAttribute("idAlmacen", idAlmacen);

        String nombreAlmacen = almacenRepository.findNombreById(idAlmacen);
        List<ProductoInventarioView> productos = inventarioRepository.findProductosPorAlmacen(idAlmacen);

        model.addAttribute("nombreAlmacen", nombreAlmacen);
        model.addAttribute("productos", productos);
        model.addAttribute("nombreAlmacen", nombreAlmacen);
        return "inventario";
    }
}
