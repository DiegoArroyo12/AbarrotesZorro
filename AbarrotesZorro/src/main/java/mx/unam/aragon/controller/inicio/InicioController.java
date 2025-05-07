package mx.unam.aragon.controller.inicio;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class InicioController {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @GetMapping("/inicio")
    public String mostrarInicio(Model model, Principal principal) {
        String username = principal.getName(); // Obtenemos el username

        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        model.addAttribute("nombreEmpleado", empleado.getNombre());
        return "inicio";
    }
}
