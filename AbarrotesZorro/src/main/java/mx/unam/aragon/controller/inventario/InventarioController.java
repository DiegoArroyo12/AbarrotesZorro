package mx.unam.aragon.controller.inventario;

import mx.unam.aragon.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class InventarioController {

    @Autowired
    InventarioRepository inventarioRepository;

    @GetMapping("/inventario")
    public String mostrarInventario(Model model, Principal principal) {
        /* Mantener el empleado que inicio sesión */
        return "inventario";
    }
}
