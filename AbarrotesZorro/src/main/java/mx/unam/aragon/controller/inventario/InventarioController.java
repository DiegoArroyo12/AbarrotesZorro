package mx.unam.aragon.controller.inventario;

import mx.unam.aragon.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InventarioController {

    @Autowired
    InventarioRepository inventarioRepository;

    @GetMapping("/inventario")
    public String mostrarInventario(@RequestParam("idAlmacen") Integer idAlmacen, Model model) {
        model.addAttribute("inventario", inventarioRepository.findByAlmacen(idAlmacen));
        model.addAttribute("idAlmacen", idAlmacen);
        return "inventario";
    }
}
