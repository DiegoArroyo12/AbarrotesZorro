package mx.unam.aragon.controller.inicio;

import mx.unam.aragon.model.entity.ClienteEntity;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import mx.unam.aragon.repository.EmpleadoRepository;
import mx.unam.aragon.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class InicioController {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    ClienteService clienteService;

    @Autowired
    AlmacenRepository almacenRepository;

    @GetMapping("/inicio")
    public String mostrarInicio(@RequestParam(required = false) String correo, Model model, Principal principal) {
        String username = principal.getName(); // Obtenemos el username

        // Para continuar con el cliente creado
        if (correo != null && !correo.isEmpty()) {
            ClienteEntity cliente = clienteService.findByCorreo(correo);
            if (cliente != null) {
                model.addAttribute("nombreCliente", cliente.getNombre());
            }
        }

        // Almacenes
        model.addAttribute("almacenes", almacenRepository.findAll());

        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        model.addAttribute("nombreEmpleado", empleado.getNombre());
        return "inicio";
    }
}
