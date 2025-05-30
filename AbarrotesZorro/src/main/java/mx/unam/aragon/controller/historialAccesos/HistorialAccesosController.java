package mx.unam.aragon.controller.historialAccesos;

import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.repository.HistorialAccesoRepository;
import mx.unam.aragon.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HistorialAccesosController {

    @Autowired
    HistorialAccesoRepository historialAccesoRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @GetMapping("empleados/historial_accesos")
    public String mostrarAccesos(@RequestParam("idSucursal") String idSucursal,
                                 @RequestParam("idUsuario") Long idUsuario,
                                 Model model) {
        // Nombre de sucursal
        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal)).map(SucursalEntity::getNombre).orElse("Nombre no encontrado");

        // Accesos
        model.addAttribute("accesos", historialAccesoRepository.findBySucursalWithDetails(Integer.parseInt(idSucursal)));
        model.addAttribute("nombreSucursal", nombreSucursal);
        model.addAttribute("idUsuarioActual", idUsuario);
        model.addAttribute("idSucursal", idSucursal);
        return "historial_accesos";
    }
}
