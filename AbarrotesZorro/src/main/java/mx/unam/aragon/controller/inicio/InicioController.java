package mx.unam.aragon.controller.inicio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
    @GetMapping("/inicio")
    public String mostrarInicio() {
        return "inicio/inicio";
    }
}
