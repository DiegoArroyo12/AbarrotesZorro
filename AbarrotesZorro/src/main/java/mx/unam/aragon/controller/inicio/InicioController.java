package mx.unam.aragon.controller.inicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.unam.aragon.model.entity.ClienteEntity;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.repository.SucursalRepository;
import mx.unam.aragon.repository.CajaRepository;
import mx.unam.aragon.repository.EmpleadoRepository;
import mx.unam.aragon.service.cliente.ClienteService;
import mx.unam.aragon.service.historialacceso.HistorialAccesoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class InicioController {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    ClienteService clienteService;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    CajaRepository cajaRepository;

    @Autowired
    private HistorialAccesoService historialAccesoService;

    @GetMapping("/registrar-entrada")
    public String registrarEntrada(Principal principal) {
        historialAccesoService.registrarEntradaInicial(principal.getName());
        return "redirect:/inicio";
    }

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

        // Cajas
        model.addAttribute("cajas", cajaRepository.findAll());

        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        model.addAttribute("nombreEmpleado", empleado.getNombre());
        model.addAttribute("nombreSucursal", empleado.getSucursal().getNombre());
        model.addAttribute("idSucursal", empleado.getSucursal().getId());

        return "inicio";
    }

    @GetMapping("/custom-logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        historialAccesoService.registrarSalida(username);

        request.logout(); // Cierra sesión en Spring
        return "redirect:/login?logout";
    }

    @PostMapping("/registro-acceso")
    @ResponseBody
    public String registrarAccesoAlmacen(@RequestParam("idSucursal") Integer idSucursal,
                                         @RequestParam(value = "idCaja", required = false) Integer idCaja,
                                         Principal principal) {

        EmpleadoEntity empleado = empleadoRepository.findByUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        historialAccesoService.registrarAcceso(empleado.getId().intValue(), idCaja, idSucursal);
        return "OK";
    }
}
